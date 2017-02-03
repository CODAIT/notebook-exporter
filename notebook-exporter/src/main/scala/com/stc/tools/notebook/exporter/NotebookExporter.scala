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


import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.Paths
import java.util.Calendar
import java.util.jar.{Attributes, JarEntry, JarOutputStream, Manifest}

import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.{AbstractFile, VirtualDirectory, VirtualFile}

object NotebookExporter {
  val generator = ApplicationGenerator

  def export(notebook: Notebook, mainClass: String, jarName: String): Unit = {

    val generatedSource: BatchSourceFile = generator.generateClass(notebook, mainClass)

    val target = new VirtualDirectory("(memory)", None)
    val compiler = new RuntimeCompiler(target)
    compiler.compile(generatedSource)

    val jarWriter = new ApplicationWritter()
    jarWriter.writeApplication(mainClass, target, jarName, true)
  }
}
