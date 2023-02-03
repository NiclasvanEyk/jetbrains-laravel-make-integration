<p align="center">
    <img src="./src/main/resources/icons/make-logo.svg" width="200px" height="200px" />
</p>

<p align="center">
    <a href="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/actions"><img alt="Build" src="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/workflows/Build/badge.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Version" src="https://img.shields.io/jetbrains/plugin/v/14612-laravel-make-integration.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Downloads" src="https://img.shields.io/jetbrains/plugin/d/14612-laravel-make-integration.svg"/></a>
    <a href="https://github.com/sponsors/NiclasvanEyk"><img alt="Sponsors" src="https://img.shields.io/github/sponsors/NiclasvanEyk"/></a>
</p>

<h1 align="center"> Make for Laravel </h1>

!["New File" menu with Laravel group provided by this plugin](https://plugins.jetbrains.com/files/14612/screenshot_b321b1b4-2b8d-45d4-92a0-8d24af629349)

<!-- Plugin description -->
**Make for Laravel** connects your Laravel application to PhpStorm:

- **["File > New"-menu entries for `artisan make:*` commands.](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/make-context-menu.md)** Generate Laravel components the way you also would create new PHP classes. Includes handy features like pre-filling namespaces based on the file hierarchy and autocompletion for command line options, such as `artisan make:controller --api`. 
- **[Laravel tool window.](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/tool-windows.md)** View all available routes and commands at a glance. Includes the usual Jetbrains goodies such as search-as-you-type, go to definition and more.
- **[Laravel Sail Autoconfiguration.](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/sail-autoconfiguration.md)** Debugging, running tests and npm scripts all work magically when developing locally, but quickly falls apart when using containers. Sail Autoconfiguration teaches PHPStorm to use the containerized versions instead, so everything just works again.
- **[Gutter icons highlighting route action.](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/route-action-icons.md)** Are you struggling with large controllers and don't know which methods are actual endpoints? Make for Laravel annotates them using an icon, which also displays the mapped URL, HTTP verb and additional Middleware.
- **[Database Autoconfiguration](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/managed-data-sources.md)** Automatically add and update the default database used by your Laravel application to the `Database` tool window.

> For additional details visit the [project on GitHub](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration).
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Make for Laravel"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>. Select the downloaded zip-file and hit "apply".

  > Note: When a new version of the plugin is available, your IDE will notify you about it regardless of how you did install it. So even when you install an update from disk once, you will still receive future updates through automated updates.

## Usage notes

This does only work if you have the Laravel project open, the root of your project is the root of your Laravel
folder and contains the artisan binary! Subfolder support may be added in a future version.

## Feature requests

If you have an idea for improving this plugin, first take a look at the
<a href="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement">existing feature requests</a>
and then submit
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues/new">an issue through Github</a>.
