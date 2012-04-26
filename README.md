sbt-simple-junit-xml-reporter-plugin
====================================

A pretty basic plugin for sbt to output junit xml reports from tests.

To use add:
   
	import sbt._

	object Plugins extends Build {
	  lazy val plugins = Project("plugins", file("."))
	    .dependsOn(
	      uri("git://github.com/bseibel/sbt-simple-junit-xml-reporter-plugin.git")
	    )
	}


to project/project/plugins.scala
