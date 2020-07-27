# Laravel Make Integration Changelog

## [Unreleased]

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
