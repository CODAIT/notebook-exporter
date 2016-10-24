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

import java.io.IOException
import java.nio.file.{Files, Paths}

import scala.reflect.runtime._
import scala.tools.reflect.ToolBoxFactory

import scalax.file.Path

class RuntimeCompiler (targetDirectory: String) {

  val classLoader = Thread.currentThread.getContextClassLoader

  val factory = new ToolBoxFactory[universe.type](universe) {
    // note that runtimeMirror() is not thread safe in scala =< 2.10
    override val mirror = universe.runtimeMirror(classLoader)
  }

  if (Files.exists(Paths.get(targetDirectory)) == true) {
    println("Existing target directory will be cleaned : " + targetDirectory) //scalastyle:ignore

    // remove all files from target folder recursively
    Path.fromString(targetDirectory).deleteRecursively(true, true)

    // recreate target folder for new compilation
    Files.createDirectory(Paths.get(targetDirectory))
  } else {
    println("Creating target directory : " + targetDirectory) //scalastyle:ignore

    // create target folder for new compilation
    Files.createDirectory(Paths.get(targetDirectory))
  }

  val toolbox = factory.mkToolBox(options = "-d " + targetDirectory)

  def compile(sourceFile: String): Unit = {
    val sourceTemplate = "/templates/NotebookApplication.scala"
    val sourceFile = getClass().getResource(sourceTemplate).getPath
    val sourceCode = scala.io.Source.fromFile(sourceFile.toString).mkString

    val tree = toolbox.parse(sourceCode)

    toolbox.compile(tree)
  }
}

object RuntimeCompiler;


