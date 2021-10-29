package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.toolWindow

import com.intellij.ui.JBColor
import com.intellij.ui.SideBorder
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.niclas_van_eyk.laravel_make_integration.common.jetbrains.ui.HtmlPanel
import com.niclas_van_eyk.laravel_make_integration.common.laravel.commands.withoutGlobalOptions
import com.niclas_van_eyk.laravel_make_integration.common.string.paragraphs
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.commands.introspection.Command
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import javax.swing.JComponent

// Sometimes table columns are very  close together, so we pad them with 4 spaces
private const val TABLE_PADDING = "&nbsp;&nbsp;&nbsp;"

class CommandDocumentation {
    companion object {
        fun empty() = JBPanelWithEmptyText()
            .withEmptyText("Select a command to see more information")

        fun forCommand(command: Command?): JComponent {
            if (command == null) {
                return empty()
            }

            val panel = HtmlPanel()
            panel.content = createHTML().div {
                h2 { +command.usageSummary }
                command.description.paragraphs().forEach { p { +it } }

                if (command.hasArgumentDescriptions) {
                    p {}
                    h4 { +"Arguments" }
                    table {
                        tbody {
                            command.definition.arguments.values
                                .forEach { argument ->
                                    tr {
                                        td { unsafe {
                                            raw(
                                                argument.name
                                                    + (if (!argument.isRequired) " (optional)" else "")
                                                    + TABLE_PADDING
                                            )
                                        } }
                                        // The raw part gets rid of <info> tags used to highlight
                                        // certain words on the console
                                        td { unsafe { raw(argument.description) } }
                                    }
                                }
                        }
                    }
                }

                if (command.definition.options.values.withoutGlobalOptions.isNotEmpty()) {
                    p {}
                    h4 { +"Options" }
                    table {
                        tbody {
                            command.definition.options.values
                                .withoutGlobalOptions
                                .forEach { option ->
                                    tr {
                                        td { +(if (!option.shortcut.isNullOrBlank()) "${option.shortcut}," else "") }
                                        td { unsafe { raw("${option.name}${TABLE_PADDING}") } }
                                        // The raw part gets rid of <info> tags used to highlight
                                        // certain words on the console
                                        td { unsafe { raw(option.description) } }
                                    }
                                }
                        }
                    }
                }
            }

            return JBScrollPane(panel).apply {
                border = SideBorder(JBColor.border(), SideBorder.NONE)
            }
        }
    }
}

private val Command.usageSummary: String
    get() {
        val requiredArguments = definition.arguments.values
            .filter { it.isRequired }
            .map { "<${it.name}>" }

        val optionalArguments = definition.arguments.values
            .filter { !it.isRequired }
            .map { "[<${it.name}>]" }

        val parts = mutableListOf("artisan", name)
        parts.addAll(requiredArguments)
        parts.addAll(optionalArguments)

        return parts.filter { it.isNotBlank() }.joinToString(" ")
    }

private val Command.hasArgumentDescriptions: Boolean
    get() = definition.arguments.values
        .map { it.description }
        .any { it.isNotBlank() }
