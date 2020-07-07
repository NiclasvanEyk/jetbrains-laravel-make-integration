# Laravel Make Integration

This package integrates the <code>php artisan make:*</code> commands to the "File > New" menu.

![](https://plugins.jetbrains.com/files/14612/screenshot_22560.png)

Once an action, say "File > New > Laravel > Controller" is triggered, a dialog pops up, where you can enter the
name of the class to be generated. Once you hit enter, the artisan command, here
<code>php artisan make:controller</code>, will be executed by your local <code>php</code> interpreter.

<h3>Helpful guidance</h3>

The plugin tries to be helpful, by <b>highlighting appropriate actions</b>, based on where in your project you
triggered the action. If you right-click click on the "app/Http" folder, only <code>Controller</code>,
<code>Middleware</code> and <code>Request</code> would be active, so you are not getting overwhelmed by all the
other make-commands available.

<blockquote>
<b>Note:</b> You can always trigger the actions from anywhere by using the double-shift/search anything menu and
search for the action. In this case, nothing will be filtered out based on your selection in the Project-view.
</blockquote>

It also <b>pre-fills the namespace</b> for the class to be generated. If you try to generate a new
<code>Job</code> in an "app/Jobs/Images/Resizing" folder, the popup will already be pre-filled with the right
namespace, here <code>Images/Resizing/</code>. Now the artisan command to be executed will contain the namespace,
so your new class will also be created in the "app/Jobs/Images/Resizing" directory.

<h3>Supported Commands</h3>

This way the following types of classes can be generated:

<ul>
    <li><code>Cast</code></li>
    <li><code>Channel</code></li>
    <li><code>Command</code></li>
    <li><code>Component</code></li>
    <li><code>Controller</code></li>
    <li><code>Event</code></li>
    <li><code>Exception</code></li>
    <li><code>Factory</code></li>
    <li><code>Job</code></li>
    <li><code>Listener</code></li>
    <li><code>Mail</code></li>
    <li><code>Middleware</code></li>
    <li><code>Migration</code></li>
    <li><code>Model</code></li>
    <li><code>Notification</code></li>
    <li><code>Observer</code></li>
    <li><code>Policy</code></li>
    <li><code>Provider</code></li>
    <li><code>Request</code></li>
    <li><code>Resource</code></li>
    <li><code>Rule</code></li>
    <li><code>Seeder</code></li>
</ul>

<h3>Usage notes</h3>

This does only work if you have the Laravel project open, the root of your project is the root of your Laravel
folder and contains the artisan binary!

<h3>Feature requests</h3>

If you have an idea for improving this plugin, first take a look at the
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues">existing feature requests</a>
and then submit
<a href="https://github.com/NiclasvanEyk/intellij-artisan-make-integration/issues/new">an issue through Github</a>.