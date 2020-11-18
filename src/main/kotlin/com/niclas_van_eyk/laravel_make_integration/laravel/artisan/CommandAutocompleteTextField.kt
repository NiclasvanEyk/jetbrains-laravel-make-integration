package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.TextFieldWithAutoCompletionListProvider

/**
 * An input that suggests the user the passed commands for better discoverability.
 */
class CommandAutocompleteTextField(
        project: Project,
        availableCommands: MutableList<Option>,
        initialInput: String = ""
) : TextFieldWithAutoCompletion<Option>(
        project,
        CommandAutocompletionProvider(withoutUnnecessaryOptions(availableCommands)),
        false,
        initialInput
) {
    companion object {
        /**
         * Filters out options, that make no sense to display everytime.
         *
         * These options are part of nearly every command, so the user most likely
         * knows them. Additionally flags like --help and --version would prevent
         * the creation of any files, so the user most likely does not want to pass
         * them, since this extension should generate files and not display versions.
         */
        fun withoutUnnecessaryOptions(availableCommands: MutableList<Option>): MutableList<Option> {
            return availableCommands.filter {
                !listOf(
                        "--help",
                        "--version",
                        "--verbose",
                        "--ansi",
                        "--no-ansi",
                        "--no-interaction",
                        "--quiet"
                ).contains(it.name)
            }.toMutableList()
        }
    }

    private class CommandAutocompletionProvider(
            availableOptions: MutableCollection<Option>
    ) : TextFieldWithAutoCompletionListProvider<Option>(availableOptions) {
        override fun getLookupString(option: Option): String {
            return if (option.type == OptionType.Flag) option.name
                   else option.nameWithoutHint + "="
        }

        override fun getTypeText(option: Option): String? {
            return option.type.name
        }

        override fun createLookupBuilder(option: Option): LookupElementBuilder {
            val lookup = super.createLookupBuilder(option)
                    .withTailText(if (!option.description.isBlank()) "  " + option.description else null, true)
                    .withPresentableText(option.nameWithoutHint)

            if (option.shortForm != null) {
                lookup.withLookupStrings(option.shortForm.split("|"))
            }

            return lookup
        }
    }
}