# {{name}}

## ClojureScript on Node.js

This template is just to gather together as much information as
possible about running ClojureScript effectively under Node.js.

All of this information is current as of Sept. 20th, 2014, although
it's likely to change rapidly due to the rather bleeding-edge nature
of both ClojureScript and Node.js, not to mention other related
technologies such as core.async, and to some extent Clojure itself.

Once you get the development process dialled, writing Node.js code
in ClojureScript is not just easy, it's fun and FAST:  No more
frustrating JVM startup times.

## Structure

The basic structure of the template here is:

    ├── README.md
    ├── project.clj
    ├── resources
    │   └── config
    │       └── development.js
    ├── run.js
    ├── test-runner.js
    ├── test
    │   └── foo.cljs
    └── src
        ├── cljs
        │   └── node_cljs
        │       ├── config.cljs
        │       ├── core.cljs
        │       ├── log.cljs
        │       └── web.cljs
        └── js
            └── some_node_module.js

There are cljs/ and js/ directories in the src/ directory - this is
because you will most probably have files of both types in your project.
This is slightly problematic because the cljs compiler will spit out
.js files in the :output-dir specified in the project.clj file.  When
those files are run from node.js, they're in a different location, so
it's tricky for node.js to find your .js files.  The kludgey way to
do it now is to know where your .cljs file will end up, transformed into
Javascript, and load your .js relative to that using

    (def foo-module (apply (js* "require") ["../../src/js/some_node_module"]))

Note that most documentation that you read about loading existing node modules
from cljs will seem to indicate that you can first do something like

    (ns zip.zap
      (:require [cljs.nodejs :as node]))

    (def foo-module (node/require "../../src/js/some_node_module"))

but **it won't work**.  This form is fine to load things from node\_modules
directories, but it will most certainly not work for loading .js files in
your own project.  The problem seems to lie with the fact that the .js file
that actually ultimately performs the "require" is somewhere deep in the
cljs lib, and its relative path to your module is mysterious and unknown.

## Node.js dependencies

This project.clj file uses the lein-npm plugin to maintain node.js
dependencies (rather than maintaining a separate package.json file)
but there is absolutely no requirement to do that.

But to run the example project, first you'll need to do

    lein npm install

to install its node dependencies.

## Dependency Injection and Configuration

Developing with node.js can always be a bit trying in terms of
modules and dependencies. How can code in module X, that needs
to access a MySQL DB, a redis queue, and a logger, somehow
be sure that all those have been loaded and initialised correctly
from the config.  To assist with this, I've ported Stuart Sierra's
"component" library from Clojure, which makes this stuff much, much
easier.  Take it from me - the ease with which you can set up
your different modules and dependencies between them is a massive
win.

## Development process

Much older documentation about using cljsbuild under node.js often
claims that you can't use

    :optimization :none

when you're using

    :target :nodejs

in your project.clj.  However, this changed fairly recently, and now it is
very possible.  This completely changes your development flow, because now

  * it's FAST
  * it's INCREMENTAL
  * it produces vaguely readable JS

The only trick (of course there's a trick) is that you need to use
the run.js file to start your application, because when you have

    :optimization :none

the cljs compiler doesn't generate the right code to load all the required
dependencies.

So, bearing all that in mind, in general you can just run

    lein cljsbuild auto

in one terminal, and it will pick up all changes to your cljs files and
quickly rebuild them (very, very quickly).  In another window, you can
then just do

    node run.js

and your app should fire up.  If you run the default code, it should produce:

    ;; Loading some_node_module
    foo!
    bar!
    2014-09-20 22:55:05.792 - ;; Starting ConfigComponent
    2014-09-20 22:55:05.796 - ;; Starting WebComponent
    2014-09-20 22:55:05.812 - ;; Starting NodeComponent
    2014-09-20 22:55:10.819 - ;; Stopping NodeComponent
    2014-09-20 22:55:10.819 - ;; Stopping WebComponent
    2014-09-20 22:55:10.819 - ;; Stopping ConfigComponent

If you dig into the code, you'll see some examples of how to use:

    * Stuart Sierra's Components and the Lifecycle protocol
    * Interop between your own JS and .cljs code
    * Interop between your .cljs code and Node modules installed using npm
    * How to load in some config and use it to initialise modules (this is fairly cheesy right now, but will improve greatly in the near future)

# Tests

There's a skeleton test setup that you can start adding to.  It uses
Chas Emerick's ClojureScript port of clojure.test, which is very similar
to what you expect in a regular Clojure project.  Under test/ you'll find
an example file.  Your tests are executed using node.js
by running the test-runner.js, gleaned from

https://github.com/mike-thompson-day8/cljsbuild-none-test-seed

You can run them thus:

    lein cljsbuild test node

and the output should be very similar to what you expect.  There are some
unfortunate weird behaviours dude to various invocations of javascript "main"
functions, so be aware of this if you start seeing strange things when you
run your tests.  It's still a fairly rough exercise.



# TODO

* Wrap some basic node.js functionality with ClojureScript
* Fill in some basic things missing from cljs.core, such as __slurp__

