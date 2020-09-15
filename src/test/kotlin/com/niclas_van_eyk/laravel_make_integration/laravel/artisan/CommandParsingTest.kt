package com.niclas_van_eyk.laravel_make_integration.laravel.artisan

import org.junit.Assert.*
import org.junit.jupiter.api.Test

internal class CommandParsingTest {
    companion object {
        val SAMPLE_OUTPUT = """
Laravel Framework 8.0.0

Usage:
  command [options] [arguments]

Options:
  -h, --help            Display this help message
  -q, --quiet           Do not output any message
  -V, --version         Display this application version
      --ansi            Force ANSI output
      --no-ansi         Disable ANSI output
  -n, --no-interaction  Do not ask any interactive question
      --env[=ENV]       The environment the command should run under
  -v|vv|vvv, --verbose  Increase the verbosity of messages: 1 for normal output, 2 for more verbose output and 3 for debug

Available commands:
  clear-compiled                        Remove the compiled class file
  down                                  Put the application into maintenance / demo mode
  env                                   Display the current framework environment
  help                                  Displays help for a command
  inspire                               Display an inspiring quote
  list                                  Lists commands
  migrate                               Run the database migrations
  optimize                              Cache the framework bootstrap files
  serve                                 Serve the application on the PHP development server
  test                                  Run the application tests
  tinker                                Interact with your application
  up                                    Bring the application out of maintenance mode
 auth
  auth:clear-resets                     Flush expired password reset tokens
 cache
  cache:clear                           Flush the application cache
  cache:forget                          Remove an item from the cache
  cache:table                           Create a migration for the cache database table
 config
  config:cache                          Create a cache file for faster configuration loading
  config:clear                          Remove the configuration cache file
 db
  db:seed                               Seed the database with records
  db:wipe                               Drop all tables, views, and types
 event
  event:cache                           Discover and cache the application's events and listeners
  event:clear                           Clear all cached events and listeners
  event:generate                        Generate the missing events and listeners based on registration
  event:list                            List the application's events and listeners
 jetstream
  jetstream:install                     Install the Jetstream components and resources
 key
  key:generate                          Set the application key
 livewire
  livewire:configure-s3-upload-cleanup  Configure temporary file upload s3 directory to automatically cleanup files older than 24hrs.
  livewire:copy                         Copy a Livewire component
  livewire:delete                       Delete a Livewire component
  livewire:discover                     Regenerate Livewire component auto-discovery manifest
  livewire:make                         Create a new Livewire component
  livewire:move                         Move a Livewire component
  livewire:publish                      Publish Livewire configuration
  livewire:stubs                        Publish Livewire stubs
 make
  make:cast                             Create a new custom Eloquent cast class
  make:channel                          Create a new channel class
  make:command                          Create a new Artisan command
  make:component                        Create a new view component class
  make:controller                       Create a new controller class
  make:event                            Create a new event class
  make:exception                        Create a new custom exception class
  make:factory                          Create a new model factory
  make:job                              Create a new job class
  make:listener                         Create a new event listener class
  make:livewire                         Create a new Livewire component
  make:mail                             Create a new email class
  make:middleware                       Create a new middleware class
  make:migration                        Create a new migration file
  make:model                            Create a new Eloquent model class
  make:notification                     Create a new notification class
  make:observer                         Create a new observer class
  make:policy                           Create a new policy class
  make:provider                         Create a new service provider class
  make:request                          Create a new form request class
  make:resource                         Create a new resource
  make:rule                             Create a new validation rule
  make:seeder                           Create a new seeder class
  make:test                             Create a new test class
 migrate
  migrate:fresh                         Drop all tables and re-run all migrations
  migrate:install                       Create the migration repository
  migrate:refresh                       Reset and re-run all migrations
  migrate:reset                         Rollback all database migrations
  migrate:rollback                      Rollback the last database migration
  migrate:status                        Show the status of each migration
 notifications
  notifications:table                   Create a migration for the notifications table
 optimize
  optimize:clear                        Remove the cached bootstrap files
 package
  package:discover                      Rebuild the cached package manifest
 queue
  queue:batches-table                   Create a migration for the batches database table
  queue:failed                          List all of the failed queue jobs
  queue:failed-table                    Create a migration for the failed queue jobs database table
  queue:flush                           Flush all of the failed queue jobs
  queue:forget                          Delete a failed queue job
  queue:listen                          Listen to a given queue
  queue:restart                         Restart queue worker daemons after their current job
  queue:retry                           Retry a failed queue job
  queue:retry-batch                     Retry the failed jobs for a batch
  queue:table                           Create a migration for the queue jobs database table
  queue:work                            Start processing jobs on the queue as a daemon
 route
  route:cache                           Create a route cache file for faster route registration
  route:clear                           Remove the route cache file
  route:list                            List all registered routes
 schedule
  schedule:run                          Run the scheduled commands
 schema
  schema:dump                           Dump the given database schema
 session
  session:table                         Create a migration for the session database table
 storage
  storage:link                          Create the symbolic links configured for the application
 stub
  stub:publish                          Publish all stubs that are available for customization
 vendor
  vendor:publish                        Publish any publishable assets from vendor packages
 view
  view:cache                            Compile all of the application's Blade templates
  view:clear                            Clear all compiled view files

"""
    }

    @Test
    fun parsesCommandsCorrectly() {
        val parsed = parseArtisanMakeCommandNames(SAMPLE_OUTPUT)
        assertEquals(listOf(
                "make:cast",
                "make:channel",
                "make:command",
                "make:component",
                "make:controller",
                "make:event",
                "make:exception",
                "make:factory",
                "make:job",
                "make:listener",
                "make:livewire",
                "make:mail",
                "make:middleware",
                "make:migration",
                "make:model",
                "make:notification",
                "make:observer",
                "make:policy",
                "make:provider",
                "make:request",
                "make:resource",
                "make:rule",
                "make:seeder",
                "make:test"
        ), parsed)
    }
}