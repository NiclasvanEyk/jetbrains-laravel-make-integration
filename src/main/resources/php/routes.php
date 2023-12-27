<?php

use Illuminate\Routing\Route;
use ReflectionFunction;

class RoutesJson
{
    public function handle()
    {
        $routes = [];
        foreach ($this->routes() as $route) {
            $routes[] = [
                'name' => $route->getName(),
                'verbs' => $route->methods(),
                'path' => ($path = $route->uri()) === "/" ? $path : "/$path",
                'action' => $this->action($route),
                'middleware' => $route->gatherMiddleware(),
            ];
        }

        echo "<MAKE_FOR_LARAVEL_OUTPUT:BEGIN>";
        echo collect($routes)->toJson();
        echo "<MAKE_FOR_LARAVEL_OUTPUT:END>";
    }

    /**
     * @return list<Route>
     */
    private function routes()
    {
        return app('router')->getRoutes()->getRoutes();
    }

    private function action(Route $route)
    {
        $action = $route->getAction();

        if (array_key_exists('controller', $action)) {
            $uses = str($action['uses']);

            return [
                'type' => 'method',
                'controller' => $uses->beforeLast('@'),
                'method' => $uses->afterLast('@'),
            ];
        }

        $closure = new ReflectionFunction($action['uses']);

        return [
            'type' => 'closure',
            'file' => str($closure->getFileName())->afterLast(base_path()),
            'start' => $closure->getStartLine(),
            'end' => $closure->getEndLine(),
        ];
    }
}

(new RoutesJson())->handle();