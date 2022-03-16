package com.github.niclasvaneyk.laravelmake.plugin.laravel.routes

import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.LoadedState
import com.github.niclasvaneyk.laravelmake.common.laravel.introspection.RevalidatedState
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.IntrospectedRoute
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RouteIntrospecter
import com.github.niclasvaneyk.laravelmake.plugin.laravel.routes.introspection.RoutesFileChangeListener
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.codeInsight.daemon.LineMarkerProviders
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level.PROJECT
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.php.lang.PhpLanguage
import com.jetbrains.php.lang.psi.PhpFile
import java.util.*

/**
 * Connects certain pieces concerning the routes and ensures proper cleanup.
 */
@Service(PROJECT)
class LaravelMakeRouteProjectService(private val project: Project): Disposable {
    private lateinit var introspecter: RouteIntrospecter
    private lateinit var refreshLineMarkersDisposable: io.reactivex.rxjava3.disposables.Disposable

    fun initialize(introspecter: RouteIntrospecter) {
        if (this::introspecter.isInitialized) return
        this.introspecter = introspecter

        refreshRoutesOnRouteFileChanges(introspecter)
        refreshLineMarkersOnRouteUpdate(introspecter)
    }

    private fun refreshRoutesOnRouteFileChanges(introspecter: RouteIntrospecter) {
        VirtualFileManager.getInstance().addAsyncFileListener(
            RoutesFileChangeListener(introspecter),
            this,
        )
    }

    private fun refreshLineMarkersOnRouteUpdate(introspecter: RouteIntrospecter) {
        refreshLineMarkersDisposable = introspecter.introspectionState
            .mapOptional {
                if (it is LoadedState) return@mapOptional Optional.of(it.result)
                if (it is RevalidatedState) return@mapOptional Optional.of(it.result)
                return@mapOptional Optional.empty()
            }
            .subscribe {
                // This works, but triggers a re-highlighting of all open files and more
                // work, that in theory is not necessary.
                // FIXME: On the intellij-platform slack it was suggested to look into
                //        `com.intellij.lang.annotation.ExternalAnnotator`. There are
                //        several examples  to be found on the platform explorer.
                DaemonCodeAnalyzer.getInstance(project).restart()
            }
    }

    override fun dispose() {
        if (this::refreshLineMarkersDisposable.isInitialized) {
            this.refreshLineMarkersDisposable.dispose()
        }
    }
}