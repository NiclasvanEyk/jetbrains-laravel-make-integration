# Laravel Make Integration Changelog

## [Unreleased]
### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security
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
