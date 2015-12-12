Slick Workshop for Scala Exchange 2015
======================================

Slides and companion material for the Slick workshop from [Scala Exchange 2015].

Copyright 2015 [Dave Gurnell] of [Underscore].
Slides and course content licensed [CC-BY-NC-SA 4.0],
code samples licensed [Apache 2.0].

[![Join the chat at https://gitter.im/underscoreio/scalax15-slick](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/underscoreio/scalax15-slick?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Quick Start

A [screencast] of the workshop content is available on Vimeo.

Follow the instructions below to get set up.
Doing so will prevent you having to download the internet on conference wifi!
You will need a Java 8 compatible JVM and a familiar programmer's text editor or IDE.
If you have any problems please let me know on the [Gitter channel].

1. Clone this repo and switch to the root directory:

    ~~~ bash
    $ git clone https://github.com/underscoreio/scalax15-slick.git

    $ cd scalax15-slick
    ~~~

2. Run SBT:

    ~~~ bash
    $ ./sbt.sh # ".\sbt.bat" on Windows
    ~~~

3. Compile and run the example "helloworld.Main" application.
   This will take a few minutes to run the first time.
   You'll need an internet connection to download dependencies:

   ~~~ bash
   sbt> runMain helloworld.Main
   ~~~

4. If you see a list of albums similar to the following, you're good:

    ~~~
    Album(Keyboard Cat,Keyboard Cat's Greatest Hits,1)
    Album(Spice Girls,Spice,2)
    Album(Rick Astley,Whenever You Need Somebody,3)
    Album(Manowar,The Triumph of Steel,4)
    Album(Justin Bieber,Believe,5)
    ~~~

   If not, let me know on the [Gitter channel].

5. If you use an IDE that requires further setup, do that now.
   I've included the `sbteclipse` and `ensime-sbt` plugins in the build.

Looking forward to seeing you at the workshop!

# Further Reading

The content for the workshop will be based heavily on the book
[Essential Slick] by Richard Dallaway and Jonathan Ferguson.

Sign up to the [Underscore newsletter] to receive news, views,
and code samples about Scala, Slick, and functional programming.

[screencast]: https://vimeo.com/148074461
[Essential Slick]: http://underscore.io/books/essential-slick
[Scala Exchange 2015]: http://scala.exchange
[Dave Gurnell]: http://davegurnell.com
[Underscore]: http://underscore.io
[CC-BY-NC-SA 4.0]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[Apache 2]: http://www.apache.org/licenses/LICENSE-2.0
[Underscore newsletter]: http://underscore.io/newsletter.html
[Gitter channel]: https://gitter.im/underscoreio/scalax15-slick