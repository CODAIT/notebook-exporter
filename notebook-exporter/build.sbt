/*
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

scalaVersion := "2.11.8"

// Spark dependencies as provided as they are available in spark runtime
val json4sDependency = "3.2.11"
val sparkDependency = "1.6.2"
val zeppelinDependency = "0.6.2"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies += "org.scala-lang" % "scala-library"  % scalaVersion.value
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.6" // 7.2.6
libraryDependencies += "org.json4s" %% "json4s-core" % json4sDependency
libraryDependencies += "org.json4s"  %% "json4s-jackson" % json4sDependency
libraryDependencies += "org.json4s"  %% "json4s-scalaz" % json4sDependency

libraryDependencies += "org.apache.zeppelin" % "zeppelin-zengine" % zeppelinDependency

libraryDependencies += "org.apache.spark"  %% "spark-core" % sparkDependency % "provided" exclude("org.scalatest", "scalatest") //scalastyle:ignore
libraryDependencies += "org.apache.spark"  %% "spark-sql" % sparkDependency % "provided" exclude("org.scalatest", "scalatest") //scalastyle:ignore

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

assemblyJarName in assembly := "notebook-exporter.jar"

// forking is required to export the dependency classpath to tests,
// so that the scala compiler can simply be called with
// the `-usejavacp` option
fork := true
