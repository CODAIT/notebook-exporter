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

  def export(notebook: Notebook, className: String, jarName: String): Unit = {

    val generatedSource: BatchSourceFile = generator.generateClass(notebook, className)

    val target = new VirtualDirectory("(memory)", None)
    var compiler = new RuntimeCompiler(target)
    compiler.compile(generatedSource)

    for( f <- target.iterator) {
      println(f.name) //scalastyle:ignore
    }

    val compiledSource = target.lookupName(className, false)

    // println(new String(compiledSource.toByteArray)) //scalastyle:ignore

    val manifest: Manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

    val application = new JarOutputStream(new FileOutputStream(jarName), manifest)

    val jarEntry = new JarEntry(className)
    jarEntry.setTime(Calendar.getInstance().getTime().getTime)
    application.putNextEntry(jarEntry)
    application.write(compiledSource.toByteArray)
    application.closeEntry()
    application.close()
  }
}


object Test {
  def main(args: Array[String]): Unit = {
    val targetDirectory = Paths.get("./target/scala-2.11/generated-classes")
    val notebookResource = getClass().getResource("/notebooks/zeppelin/scala-tutorial.json")
    val notebook = ZeppelinNotebook(notebookResource.getPath)
    NotebookExporter.export(notebook, "NotebookApplication.class", "generated-application.jar")
  }
}
