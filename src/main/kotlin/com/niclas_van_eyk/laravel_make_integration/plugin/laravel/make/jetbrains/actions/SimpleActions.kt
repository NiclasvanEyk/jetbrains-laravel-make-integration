package com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.jetbrains.actions

import com.niclas_van_eyk.laravel_make_integration.common.laravel.LaravelProjectPaths
import com.niclas_van_eyk.laravel_make_integration.plugin.laravel.make.SubCommand

// All of these Actions are handled the same:
//
// - They have a set location in the project
// - Once created, the file will be in PROJECT/DEFAULT_LOCATION/CLASS_NAME.php

class MakeCastAction : ArtisanMakeSubCommandAction(
    SubCommand("cast", LaravelProjectPaths.CASTS)
)

class MakeChannelAction : ArtisanMakeSubCommandAction(
    SubCommand("channel", LaravelProjectPaths.CHANNELS)
)

class MakeCommandAction : ArtisanMakeSubCommandAction(
    SubCommand("command", LaravelProjectPaths.COMMANDS)
)

class MakeComponentAction : ArtisanMakeSubCommandAction(
    SubCommand("component", LaravelProjectPaths.COMPONENTS)
)

class MakeControllerAction : ArtisanMakeSubCommandAction(
    SubCommand("controller", LaravelProjectPaths.CONTROLLERS)
)

class MakeEventAction : ArtisanMakeSubCommandAction(
    SubCommand("event", LaravelProjectPaths.EVENTS)
)

class MakeExceptionAction : ArtisanMakeSubCommandAction(
    SubCommand("exception", LaravelProjectPaths.EXCEPTIONS)
)

class MakeFactoryAction : ArtisanMakeSubCommandAction(
    SubCommand("factory", LaravelProjectPaths.FACTORIES)
)

class MakeJobAction : ArtisanMakeSubCommandAction(
    SubCommand("job", LaravelProjectPaths.JOBS)
)

class MakeListenerAction : ArtisanMakeSubCommandAction(
    SubCommand("listener", LaravelProjectPaths.LISTENERS)
)

class MakeMailAction : ArtisanMakeSubCommandAction(
    SubCommand("mail", LaravelProjectPaths.MAILS)
)

class MakeMiddlewareAction : ArtisanMakeSubCommandAction(
    SubCommand("middleware", LaravelProjectPaths.MIDDLEWARE)
)

class MakeNotificationAction : ArtisanMakeSubCommandAction(
    SubCommand("notification", LaravelProjectPaths.NOTIFICATIONS)
)

class MakeObserverAction : ArtisanMakeSubCommandAction(
    SubCommand("observer", LaravelProjectPaths.OBSERVERS)
)

class MakePolicyAction : ArtisanMakeSubCommandAction(
    SubCommand("policy", LaravelProjectPaths.POLICIES)
)

class MakeProviderAction : ArtisanMakeSubCommandAction(
    SubCommand("provider", LaravelProjectPaths.PROVIDERS)
)

class MakeRequestAction : ArtisanMakeSubCommandAction(
    SubCommand("request", LaravelProjectPaths.REQUESTS)
)

class MakeResourceAction : ArtisanMakeSubCommandAction(
    SubCommand("resource", LaravelProjectPaths.RESOURCES)
)

class MakeRuleAction : ArtisanMakeSubCommandAction(
    SubCommand("rule", LaravelProjectPaths.RULES)
)

class MakeSeederAction : ArtisanMakeSubCommandAction(
    SubCommand("seeder", LaravelProjectPaths.SEEDS)
)

// VENDOR
class MakeLivewireAction : ArtisanMakeSubCommandAction(
    SubCommand("livewire", LaravelProjectPaths.LIVEWIRE)
)
