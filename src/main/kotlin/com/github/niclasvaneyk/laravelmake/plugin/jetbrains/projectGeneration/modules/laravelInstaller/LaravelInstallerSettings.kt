package com.github.niclasvaneyk.laravelmake.plugin.jetbrains.projectGeneration.modules.laravelInstaller

enum class JetstreamStack(val value: String) {
    Livewire("livewire"),
    Inertia("inertia"),
}

data class JetstreamOptions(
    /** The Jetstream stack that should be installed */
    val stack: JetstreamStack,

    /** Indicates whether Jetstream should be scaffolded with team support */
    val teams: Boolean,

    /** Issues a prompt to determine if Jetstream should be installed */
    val prompt: Boolean,
)

data class GithubOptions (
    /** The GitHub organization to create the new repository for */
    val organization: String,
)

data class GitOptions (
    /** The branch that should be created for a new repository */
    val branch: String,
)

/** https://github.com/laravel/installer/blob/master/src/NewCommand.php */
data class LaravelInstallerSettings(
    /** Path to the globally installed laravel installer */
    val executablePath: String,

    /** The name of the generated application */
    val projectName: String,

    /** Installs the latest "development" release */
    val dev: Boolean,

    /** Forces install even if the directory already exists */
    val force: Boolean,

    val gitOptions: GitOptions,
    val githubOptions: GithubOptions?,
    val jetstreamOptions: JetstreamOptions?,
) {
    /** Create a new repository on GitHub */
    val createGithubRepository get() = githubOptions !== null

    /** Initialize a Git repository */
    val initializeGitRepository get() = githubOptions !== null

    /** Installs the Laravel Jetstream scaffolding */
    val installJetstream get() = jetstreamOptions !== null
}