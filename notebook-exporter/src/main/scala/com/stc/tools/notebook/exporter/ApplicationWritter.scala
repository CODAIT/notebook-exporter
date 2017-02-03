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

import java.io.{BufferedInputStream, FileInputStream, FileOutputStream, IOException}
import java.util.jar.{Attributes, JarEntry, JarOutputStream, Manifest}
import java.nio.file.{Files, Path, Paths}
import java.util.Calendar

import com.stc.utils.FileUtils

import scala.reflect.io.VirtualDirectory
import scala.tools.nsc.io.{AbstractFile, Jar, VirtualDirectory}


class ApplicationWritter () {


  def writeApplication(mainClass: String,
                       compiledDirectory: VirtualDirectory,
                       jarLocation: String,
                       overwrite: Boolean = false): Unit = {


    val jarPath = Paths.get(jarLocation)
    if (Files.exists(jarPath) == true) {
      if (overwrite) {
        // overwrite the existing application
        println("Overwritting existing application at : " + jarPath) //scalastyle:ignore
        Files.delete(jarPath)
      } else {
        throw new IOException("Application already exists : '" + jarPath + "'")
      }
    }

    val manifest: Manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainClass);

    val application = new JarOutputStream(new FileOutputStream(jarLocation), manifest)

    for (classFile <- compiledDirectory.iterator) {
      println("Adding to jar: " + classFile.name) //scalastyle:ignore

      val jarEntry = new JarEntry(classFile.name)
      jarEntry.setTime(Calendar.getInstance().getTime().getTime)
      application.putNextEntry(jarEntry)
      application.write(classFile.toByteArray)
      application.closeEntry()
    }
    application.close()
  }
}

/*
object WriteTest {
  def main(args: Array[String]): Unit = {
    val sourceFile = getClass().getResource("/templates/NotebookApplication.scala").toString
    val targetDirectory = Paths.get("./target/scala-2.11/generated-classes").toString

    val compiler = new RuntimeCompiler(targetDirectory)
    compiler.compile(sourceFile)


    val targetJar = Paths.get("./target/scala-2.11/generated-classes/application.jar").toString
    val applicationWritter = new ApplicationWritter(targetJar)


    val files = FileUtils.getFilteredLisfOfFiles(targetDirectory.toString, "class")

    for(file <- files) {
      applicationWritter.add(file.toString)
    }

    applicationWritter.close()
  }
}
*/
