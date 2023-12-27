package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection

import com.github.niclasvaneyk.laravelmake.common.jetbrains.progress.ProgressBarBuilder
import com.github.niclasvaneyk.laravelmake.common.laravel.Artisan
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CommandBasedIntrospecter
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.CouldNotExtractJsonException
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.RunCode
import com.github.niclasvaneyk.laravelmake.common.string.makeForLaravelOutput
import com.google.gson.GsonBuilder
import com.intellij.openapi.application.*
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.NonUrgentExecutor
import com.jetbrains.php.PhpIndex
import java.util.concurrent.Callable

class RouteIntrospecter(
    artisan: Artisan,
    progressBar: ProgressBarBuilder,
    private val project: Project,
) : CommandBasedIntrospecter<List<IntrospectedRoute>>(artisan, progressBar) {
    override val description = "Scanning Laravel routes"
    override val command = RunCode(
        // Pretty wonky to inline it here, but loading resources does not work at the moment and I want to finish this now, so this will do for now.
        """
use Illuminate\Routing\Route;
use ReflectionFunction;

class RoutesJson
{
    public function handle()
    {
        ${'$'}routes = [];
        foreach (${'$'}this->routes() as ${'$'}route) {
            ${'$'}routes[] = [
                'name' => ${'$'}route->getName(),
                'verbs' => ${'$'}route->methods(),
                'path' => (${'$'}path = ${'$'}route->uri()) === "/" ? ${'$'}path : "/${'$'}path",
                'action' => ${'$'}this->action(${'$'}route),
                'middleware' => ${'$'}route->gatherMiddleware(),
            ];
        }

        echo "<MAKE_FOR_LARAVEL_OUTPUT:BEGIN>";
        echo collect(${'$'}routes)->toJson();
        echo "<MAKE_FOR_LARAVEL_OUTPUT:END>";
    }

    /**
     * @return list<Route>
     */
    private function routes()
    {
        return app('router')->getRoutes()->getRoutes();
    }

    private function action(Route ${'$'}route)
    {
        ${'$'}action = ${'$'}route->getAction();

        if (array_key_exists('controller', ${'$'}action)) {
            ${'$'}uses = str(${'$'}action['uses']);

            return [
                'type' => 'method',
                'controller' => ${'$'}uses->beforeLast('@'),
                'method' => ${'$'}uses->afterLast('@'),
            ];
        }

        ${'$'}closure = new ReflectionFunction(${'$'}action['uses']);

        return [
            'type' => 'closure',
            'file' => str(${'$'}closure->getFileName())->afterLast(base_path()),
            'start' => ${'$'}closure->getStartLine(),
            'end' => ${'$'}closure->getEndLine(),
        ];
    }
}

(new RoutesJson())->handle();
        """.trimIndent()
        )

    override fun onCommandOutput(output: String, publish: (result: List<IntrospectedRoute>) -> Unit) {
        val routes: List<RouteListEntry>
        try {
            routes = GsonBuilder()
                .registerTypeAdapter(RouteAction::class.java, RouteActionAdapter())
                .create()
                .fromJson(output.makeForLaravelOutput(), Array<RouteListEntry>::class.java)
                .toList()
        } catch (exception: Throwable) {
            throw CouldNotExtractJsonException(output.makeForLaravelOutput(), exception)
        }

        ReadAction
            .nonBlocking(Callable {
                RouteListEntryEnhancer(
                    PhpIndex.getInstance(project),
                    ModuleManager.getInstance(project),
                ).enhance(routes)
            })
            .inSmartMode(project)
            .finishOnUiThread(ModalityState.NON_MODAL) { introspectedRoutes ->
                publish(introspectedRoutes)
            }
            .submit(NonUrgentExecutor.getInstance())
    }
}

