package com.github.niclasvaneyk.laravelmake.plugin.laravel.make.jetbrains

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.ui.EditorTextField
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.autoCompletion.CommandAutocompleteTextField
import com.github.niclasvaneyk.laravelmake.plugin.jetbrains.services.LaravelMakeProjectService
import com.github.niclasvaneyk.laravelmake.plugin.laravel.commands.introspection.Command
import com.github.niclasvaneyk.laravelmake.plugin.laravel.make.SubCommand
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * The dialog that prompts the user for the name + flags to pass to the make-command.
 *
 * [CommandAutocompleteTextField] is probably more interesting here.
 */
class ArtisanMakeDialog(
    private val project: Project,
    private val command: SubCommand,
    private val initialInput: InitialInputSuggestion
) : DialogWrapper(project) {
    private lateinit var editor: EditorTextField

    val input: String get() = editor.text

    init {
        title = command.description
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout(0, 4))

        val commands = retrieveCommands()
        val matchingCommand = commands.firstOrNull { it.name == command.asArtisanCommand }
        val options = matchingCommand?.definition?.options?.values?.toMutableList() ?: mutableListOf()

        editor = CommandAutocompleteTextField(project, options, initialInput.toInputString)
        editor.setPreferredWidth(250)
        panel.add(LabeledComponent.create(editor, command.capitalized), BorderLayout.SOUTH)
        return panel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return editor
    }

    private fun retrieveCommands(): MutableList<Command> {
        val service = project.getService(LaravelMakeProjectService::class.java)
        val laravelProject = service.application ?: return mutableListOf()
        val introspecter = laravelProject.introspection.commandIntrospecter

        return introspecter.snapshot?.toMutableList() ?: mutableListOf()
    }
}
