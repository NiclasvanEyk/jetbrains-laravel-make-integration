# Contributing

This file contains a short guide of useful things to know if you consider contributing.

## Concepts

Instead of static analysis, this plugin works by actually booting up the Laravel application and basically "ask" it for the real state. This of course only works, once we successfully detect how to boot the app. This is figured out when a project is opened.

If we successfully find an application, it can be accessed using `LaravelMakeProjectService.application` or via the `Project.laravel` extension property. Additionally, there are counterparts of usual IntelliJ-SDK components, such as the [`LaravelAction`](src/main/kotlin/com/github/niclasvaneyk/laravelmake/plugin/jetbrains/actionSystem/LaravelAction.kt), which only executes the action if the project contains a Laravel application and otherwise disables/hides it.

If a Laravel application was detected, we also need to know how to run PHP code. This is done through the projects PHP interpreter. Once this is also properly configured and detected, we can finally run initialization code. The [`LaravelApplicationListener`s](src/main/kotlin/com/github/niclasvaneyk/laravelmake/plugin/laravel/LaravelApplicationListener.kt) are registered in the `plugin.xml` and are run after the application is fully accessible.

## Directory Structure

Note that this is not very strict and rather an orientation where to look / find things in the project. The `common` folder houses components, that could theoretically stand on its own, such as String-utilities or data classes. Functionality that mostly is dealing with the platform code resides in `plugin.jetbrains` and code that is directly related to Laravel is placed in `plugin.laravel`. 
