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
package com.stc.tools.notebook

import java.nio.file.Paths

import com.stc.tools.notebook.exporter.{JupyterNotebook, Notebook, NotebookExporter, ZeppelinNotebook}
import joptsimple.util.KeyValuePair
import joptsimple.{OptionParser, OptionSpec}

// scalastyle:off println
object Application {

  val usage =
    """
      Usage: java -jar exporter.jar --export notebook.json --to application.jar
    """


  def validate(parser: CommandLineParser): Boolean = {
    var isValid = false

    if (parser.has(CommandLineOption.export) &&
        parser.has(CommandLineOption.to)) {
          isValid = true
    }

    return isValid
  }

  def main(args: Array[String]): Unit = {

    if (args.length == 0) {
      println("No args provided")
      println(usage)
      System.exit(1)
    }

    val parameters = new CommandLineParser(args)

    if (! validate(parameters)) {
      println("Invalid args provided: ")
      args.foreach(println)
      println(usage)
      System.exit(1)
    }


    // currently only supporting export action
    // --export notebook.json application.jar
    val notebookType = parameters.get(CommandLineOption.notebookType)
    val notebookLocations = parameters.getAll(CommandLineOption.export).map(
      p => Paths.get(p).toAbsolutePath.toString
    )
    val targetApplicationLocation =
      Paths.get(parameters.get(CommandLineOption.to)).toAbsolutePath.toString

    println(notebookLocations)
    println(targetApplicationLocation)

    if (notebookType.equalsIgnoreCase("zeppelin")) {
      var notebook = ZeppelinNotebook(notebookLocations)
      NotebookExporter.export(notebook, "NotebookApplication.scala", targetApplicationLocation)
    } else if (notebookType.equalsIgnoreCase("jupyter")) {
      var notebook = JupyterNotebook(notebookLocations)
      NotebookExporter.export(notebook, "NotebookApplication.scala", targetApplicationLocation)
    } else {
      println("Invlid notebook type :" + notebookType)
    }

    println("Application generated as " + targetApplicationLocation)
    println("Use the following hint to build your spark-submit command : ")
    println("  $SPARK_HOME/bin/spark-submit --class NotebookApplication "
      + targetApplicationLocation)
  }
}

// scalastyle:on println
