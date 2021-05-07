# Laravel Make Integration Changelog

## [Unreleased]

### Fixed
- \#22 Fixed a bug that occurred when working with attached projects 

## [2.3.4]

### Added

- \#24 A hint when you try to execute an artisan command through a remote interpreter that cannot be connected to (e.g.
  when you forgot to start the docker daemon on macOS)

### Changed

- The scanning for the available commands now works in a single pass. This means that command autocompletion and vendor
  command support is now available faster (especially if you are using docker containers!)

### Fixed
- \#28 Error when starting a project without a project interpreter


## [2.3.3]

### Added
- Experimental support for Laravel Livewire. This is one is only available from the "File > New" menu for now and only 
  after all artisan commands were detected. See [issue 10](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/issues/10) for more information about this.

### Changed
- The `--no-interaction` flag is automatically appended for each command. This prevents hanging, e.g. when a command 
  expects input from stdin, which is not possible in our context.

## [2.3.2]

### Added
- Support for the new `artisan make:test` command. Automatically adds the `--unit` flag, if you have 
  right-clicked a folder below `tests/Unit`!
- A [step-by-step guide](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/tree/master/docs/setup_with_sail.md) on how to use the plugin with Docker and Laravel Sail

### Fixed
- \#21 Cyclic service initialization
- make:model now works in newer Laravel versions, where models are created in the Models/ folder. Older versions (<8.0.0) still use the default behavior

## [2.3.1]

### Fixed

- PhpStorm does not crash anymore if you open a non-Laravel project. Thank you Jeremy for reporting this!

## [2.3.0]

### Added

- [Autocompletion for flags](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/8e2e8d67075e5e4080ad4165f41b0f2c968bc180/features.md#%EF%B8%8F-flags)

### Changed

- The [feature documentation on GitHub](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/blob/8e2e8d67075e5e4080ad4165f41b0f2c968bc180/features.md) was rewritten and moved to its own file

## [2.2.0]

### Added

- A new action to open the <kbd>File</kbd> > <kbd>New</kbd> > <kbd>Laravel</kbd> popup. Take a look at [the PR](https://github.com/NiclasvanEyk/jetbrains-laravel-make-integration/pull/8) for screenshots and more information.

### Fixed

- If you had no interpreter set, the generation would just throw an exception and fail. Now we show a little notification that prompts you to set up a PHP interpreter in Settings > Languages & Frameworks > PHP.

## [2.1.2]

### Fixed

- Fixed unchecked array access (#6)

## [2.1.1]

### Added

- The plugin now has a logo

### Changed

- Added support for the 2020.2 builds of the Jetbrains platform
  - This fixes the message `Plugin 'Laravel Make Integration' version 2.1.0 is incompatible with this installation: until build 201.SNAPSHOT < PS-202.6397.115`
    that popped up on for example PHPStorm `2020.2`. 
  - This will not happen again in the future, because the plugin does not specify the `build-until` property anymore. 
  - This fixes the problem reported in https://plugins.jetbrains.com/plugin/14612-laravel-make-integration/reviews#review=43639 Thank you RJFares for reporting this issue!

## [2.1.0]

### Changed

- The "File > New > Laravel"-group now hides in non-Laravel projects anymore

## [2.0.0]

- You can pass flags to the commands (#3), just as you would do on the command line
- The artisan binary now gets executed using the project interpreter, wich has the following impact
    - The plugin now requires a paid IDE, because it now depends on the PHP-Plugin
    - Remote Interpreters are supported (#1), so now Docker containers or SSH can be used to execute the artisan commands
- Updated the plugin infrastructure to use Gradle (should have no user-facing changes)

## [v1.1.1]

- Fixed paths for migrations and factories
- Notifications should now pop up in case something goes wrong
- The make commands now execute on another thread, so there should not be any blocking anymore while executing the artisan binary

## [v1.1.0]

Updated the documentation and package description.

## [v1.0.0]

This is the initial release, so there are no changes yet.
