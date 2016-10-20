/*
 * Copyright (c) 2016 Luciano Resende
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
name := "notebook-exporter"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype Repository" at "http://oss.sonatype.org/content/repositories/releases"

// Spark dependencies as provided as they are available in spark runtime
val json4sDependency = "3.2.11"
val sparkDependency = "2.0.1"
val zeppelinDependency = "0.6.2"

libraryDependencies += "org.apache.zeppelin" % "zeppelin-zengine" % zeppelinDependency

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.6"
libraryDependencies += "org.json4s" %% "json4s-core" % json4sDependency
libraryDependencies += "org.json4s"  %% "json4s-jackson" % json4sDependency
libraryDependencies += "org.json4s"  %% "json4s-scalaz" % json4sDependency

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

assemblyJarName in assembly := "notebook-exporter.jar"
