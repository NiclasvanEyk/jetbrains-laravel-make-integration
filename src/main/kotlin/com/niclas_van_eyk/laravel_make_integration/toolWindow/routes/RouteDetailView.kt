package com.niclas_van_eyk.laravel_make_integration.toolWindow.routes

import com.intellij.ui.components.JBPanelWithEmptyText
import javax.swing.JPanel

class RouteDetailView: JPanel() {
    init {
        add(JBPanelWithEmptyText().withEmptyText("Select a route to see more information"))
    }
}