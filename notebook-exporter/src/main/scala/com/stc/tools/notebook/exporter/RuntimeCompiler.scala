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
package com.stc.tools.notebook.exporter

import java.io.BufferedOutputStream
import java.net.{URL, URLClassLoader}
import java.nio.file.{Files, Paths}

import scala.reflect.internal.util.{BatchSourceFile, Position, SourceFile}
import scala.reflect.io.{VirtualDirectory, VirtualFile}
import scala.reflect.runtime._
import scala.tools.nsc.Global
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.ConsoleReporter
import scalax.file.Path

class RuntimeCompiler (targetDirectory: VirtualDirectory) {

  val classLoader = ClassLoader.getSystemClassLoader
  val urls = classLoader.asInstanceOf[URLClassLoader].getURLs();

  val settings = new Settings
  for (url <- urls) {
    println(url.toString) //scalastyle:ignore
    settings.classpath.append(url.toString)
  }

  settings.outputDirs.setSingleOutput(targetDirectory)

  val compiler = new Global(settings, new ConsoleReporter(settings) {
    override def printMessage(pos: Position, msg: String) = {
      println(">>> " + pos + " - " + msg ) // scalastyle:ignore
    }
  })

  def compile(sourceFile: SourceFile): Unit = {
    val run: compiler.Run = new compiler.Run

    // run.compile(List(sourceFilePath.toString))
    run.compileSources(List(sourceFile))
    // run.compileFiles(List(f.file))
  }
}

