JBoss Logging Equinox [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/com.github.marschall.equinox-jboss-logging/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/com.github.marschall.equinox-jboss-logging)  [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/com.github.marschall.equinox-jboss-logging.svg)](https://www.javadoc.io/doc/com.github.marschall/com.github.marschall.equinox-jboss-logging)
=====================

This is an implementation of JBoss Logging using the [Equinox](https://www.eclipse.org/equinox/) [ExtendedLogService](https://bugs.eclipse.org/bugs/show_bug.cgi?id=260672).

In plain words it makes all code that uses JBoss Logging and log to Equinox `.metadata/.log` log file. It does this by redirecting all the log messages to the Equinox `ExtendedLogService`. This is mostly interesting for code that runs inside Eclipse RCP applications. This does _not_ make Equinox use JBoss Logging. 

