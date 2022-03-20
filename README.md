# Laravel Make

<p align="center">
    <a href="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/actions"><img alt="Build" src="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/workflows/Build/badge.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Version" src="https://img.shields.io/jetbrains/plugin/v/14612-laravel-make-integration.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Downloads" src="https://img.shields.io/jetbrains/plugin/d/14612-laravel-make-integration.svg"/></a>
    <a href="https://github.com/sponsors/NiclasvanEyk"><img alt="Sponsors" src="https://img.shields.io/github/sponsors/NiclasvanEyk"/></a>
</p>

<!-- Plugin description -->
!["New File" menu with Laravel group provided by this plugin](https://plugins.jetbrains.com/files/14612/screenshot_b321b1b4-2b8d-45d4-92a0-8d24af629349)

Laravel Make provides a set of integration points that connect your Laravel application to PhpStorm.

- `artisan make:*` commands are integrated into the "File > New" context menu, including autocomplete for flags and pre-filled namespaces
- Laravel tool window, providing native, auto-refreshing views for `artisan route:list` supporting search-as-you-type other kinds of filters 
- Works, even if your PHP runs inside Docker, e.g. when using Laravel Sail (see [setup instructions](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/setup_with_sail.md))

> A more detailed description is located in the [project readme on GitHub](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration)
<!-- Plugin description end -->

## Getting Started

In order to the plugin to know how to interact with your application and PHP, you need to [configure your PHP interpreter](https://www.jetbrains.com/help/phpstorm/configuring-local-interpreter.html).
If you plan to develop using Laravel Sail, you can [follow our guide](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/main/docs/setup_with_sail.md) or [the one from Jetbrains](https://www.jetbrains.com/help/phpstorm/configuring-remote-interpreters.html).

The features are described in more detail on their own documentation pages:

- ["File > New"-menu entries for `artisan make:*` commands](./docs/make-context-menu.md)
- [Laravel tool window showing routes and commands](./docs/tool-windows.md)
- [Laravel Sail Autoconfiguration](./docs/sail-autoconfiguration.md)
- [Gutter icons highlighting route action](./docs/inlay-route-action-hints.md)

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "jetbrains-laravel-make-integration"</kbd> >
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
