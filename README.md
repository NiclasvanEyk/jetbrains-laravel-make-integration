# Laravel Make Integration for the Jetbrains Platform 

<!-- Plugin description -->

<p align="center">
    This package integrates the <code>php artisan make:*</code> commands to the "File > New" menu. 
</p>
<p align="center">
    <img alt="Newfile menu with Laravel group provided by this plugin" src="https://plugins.jetbrains.com/files/14612/screenshot_22560.png" />
</p>
<p align="center">
    <a href="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/actions"><img alt="Build" src="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/workflows/Build/badge.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Version" src="https://img.shields.io/jetbrains/plugin/v/14612-laravel-make-integration.svg"/></a>
    <a href="https://plugins.jetbrains.com/plugin/14612-laravel-make-integration"><img alt="Downloads" src="https://img.shields.io/jetbrains/plugin/d/14612-laravel-make-integration.svg"/></a>
    <a href="https://github.com/sponsors/NiclasvanEyk"><img alt="Sponsors" src="https://img.shields.io/github/sponsors/NiclasvanEyk"/></a>
</p>

Once an action, say <kbd>File</kbd> > <kbd>New</kbd> > <kbd>Laravel</kbd> > <kbd>Controller</kbd> is triggered, a dialog pops up, where you can enter the
name of the class to be generated. Once you hit enter, the artisan command, here
<code>php artisan make:controller</code>, will be executed by your configured `php` interpreter.

> A more detailed description can be is located in the [project readme on GitHub](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration)

<!-- Plugin description end -->

## Getting Started

If you develop using your local interpreter everything should just work.

If you are using Laravel Sail or otherwise develop locally using Docker, go to [this page](./docs/setup_with_sail.md). It contains a step-by-step guide that describes how to setup a fresh Laravel application using Laravel Sail and how to connect PHPStorm (and in turn Laravel Make Integration) to the PHP interpreter inside our Docker container.

## [Features](./features.md)

A full list of all features can be found in [here](./features.md). This also serves as the documentation at the moment.

### Supported Commands

#### Laravel default commands

- Cast
- Channel
- Command
- Component
- Controller
- Event
- Exception
- Factory
- Job
- Listener
- Mail
- Middleware
- Migration
- Model
- Notification
- Observer
- Policy
- Provider
- Request
- Resource
- Rule
- Seeder
- Test

#### Vendor Commands (experimental)

The plugin runs `php artisan help` to get a list of all commands available each time you open a Laravel project. For each command that starts with `make:` it will 
execute `php artisan make:my-command --help`, to get a list of all the parameters to provide autocompletion. If a vendor
command like `make:livewire` is detected, it will automatically be added to the "File > New > Laravel" menu.

Note that this is experimental for now and that it might take a while for all commands to be indexed. Below you can find
a list of commands that are supported for now. If you have a request for a command of a popular library to be added here,
please open an issue.

- Livewire (`artisan make:livewire`, also supports the flags, such as `--inline`)

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "jetbrains-laravel-make-integration"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>. After you de-compress the zip-file from the releases page you need to select the file `lib/jetbrains-laravel-make-integration-VERSION.jar` when installing from disk.
  
  > Note: When a new version of the plugin is available, your IDE will notify you about it regardless of how you did install it. So even when you install an update from disk once, you will still receive future updates through automated updates.

## Usage notes

This does only work if you have the Laravel project open, the root of your project is the root of your Laravel
folder and contains the artisan binary!

## Feature requests

If you have an idea for improving this plugin, first take a look at the
<a href="https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement">existing feature requests</a>
and then submit
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues/new">an issue through Github</a>.
