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

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.{Files, Paths}

import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.io.{AbstractFile, VirtualDirectory}
import scala.tools.nsc.io.PlainFile

object ApplicationGenerator {
  val template =
    s"""
       import org.apache.spark.sql.SQLContext
       import org.apache.spark.{SparkConf, SparkContext}
       // import org.apache.spark.sql.SparkSession

       trait BaseNotebookApplication {
         private var _args: Array[String] = null

         // Bootstrapping Spark Context
         protected lazy val _conf = new SparkConf().setAppName("Zeppelin Notebook Application")

         // implicit lazy val spark: SparkSession = SparkSession.builder().config(_conf).getOrCreate()
         // implicit lazy val sc: SparkContext = spark.sparkContext

         implicit lazy val sc: SparkContext = new SparkContext(_conf)
         implicit lazy val sqlContext: SQLContext = new SQLContext(sc)

         final def main(args: Array[String]): Unit = {
           // Loading Class State
           this._args = args

           // Call Business Logic
           run()
         }

         // Every Pipeline should implement this
         def run() : Unit
       }

       object NotebookApplication extends BaseNotebookApplication {

         import sqlContext.implicits._
         import sqlContext.sql
         import org.apache.spark.sql.functions._

         def run(): Unit = {
           %s
         }
       }
    """
  def generateClass(notebook: Notebook, className: String): BatchSourceFile = {
    val buffer = new StringBuilder

    for(p <- notebook.paragraphs) {
      if (p.text.trim.startsWith("%") == false) {
        buffer.append("// Paragraph " + p.id)
        buffer.append("\n")
        buffer.append(p.text)
      }
    }

    println(">>>" + template.format(buffer.toString()) + "<<<") //scalastyle:ignore
    new BatchSourceFile(className, template.format(buffer.toString()))
  }
}