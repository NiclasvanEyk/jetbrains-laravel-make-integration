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
</p>

Once an action, say <kbd>File</kbd> > <kbd>New</kbd> > <kbd>Laravel</kbd> > <kbd>Controller</kbd> is triggered, a dialog pops up, where you can enter the
name of the class to be generated. Once you hit enter, the artisan command, here
<code>php artisan make:controller</code>, will be executed by your configured `php` interpreter.

> A more detailed description can be is located in the [project readme on GitHub](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration)

<!-- Plugin description end -->

## Features

### 🎯 Helpful guidance

The plugin tries to be helpful, by **highlighting appropriate actions**, based on where in your project you
triggered the action. If you right-click on the "app/Http" folder, only `Controller`,
`Middleware`, `Resource` and `Request` would be active, so you are not getting overwhelmed by all the
other make-commands available.

![Only a part of the make-commands are active, as the action was triggered from the Http-folder](https://plugins.jetbrains.com/files/14612/screenshot_22856.png)

> **Note:** You can always trigger the actions from anywhere by using the double-shift/search anything menu and
> search for the action. In this case, nothing will be filtered out based on your selection in the Project-view.

### 🤖 Auto filled namespaces

It also **pre-fills the namespace** for the class to be generated. If you try to generate a new
`Controller` in a `A/Deeply/Nested/Namespace/In/Your/Controllers/Folder`, the input will already be pre-filled with the right
namespace, as you can see in the image below. Now the artisan command to be executed will contain the namespace,
so your new class will also be created in the `app/Http/Controllers/A/Deeply/Nested/Namespace/In/Your/Controllers/Folder`.

![The namespace was initially filled, as the action was triggered from a sub-folder of app/Http](https://plugins.jetbrains.com/files/14612/screenshot_22854.png)

### 🏳️‍🌈 Flags

You can pass every flag that is supported by the artisan sub-command of your choice. Just write all flags
*after* the name of the class you want to generate, and they will be passed along.

<img src="https://plugins.jetbrains.com/files/14612/screenshot_22853.png" alt="A modal with options passed to the make command" width="50%"></img>

### 🐳 Remote Interpreters

If you are using Docker to run your local development environment, and you have specified path mappings for your project PHP interpreter, everything will just work fine (just make sure you have your path mappings setup). The extension uses the interpreter specified in the project settings, or the first local one it can find, if none is configured.

Because the IDE has to launch a Docker container in the background, which takes some time, you will see a popup like the one below, if you are using a remote interpreter.

<img src="https://plugins.jetbrains.com/files/14612/screenshot_22855.png" alt="A waiting modal, that indicates that the extension connects to a docker container" width="50%"></img>

### Supported Commands

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

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "jetbrains-laravel-make-integration"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage notes

This does only work if you have the Laravel project open, the root of your project is the root of your Laravel
folder and contains the artisan binary!

## Feature requests

If you have an idea for improving this plugin, first take a look at the
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues">existing feature requests</a>
and then submit
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues/new">an issue through Github</a>.
