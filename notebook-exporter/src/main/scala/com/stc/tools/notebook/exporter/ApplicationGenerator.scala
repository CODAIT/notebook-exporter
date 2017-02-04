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
import scala.util.matching.Regex

private object ParagraphParser {
  private val caseClassPattern = "case class .*\\((.*?)\\)".r

  def getDeclarations(source: String): String = {
    caseClassPattern.findAllIn(source).mkString("\n")
  }

  def getCleanSource(source: String): String = {
    caseClassPattern.replaceAllIn(source, "")
  }
}

object ApplicationGenerator {
  val template =
    s"""
       import org.apache.spark.sql.SQLContext
       import org.apache.spark.{SparkConf, SparkContext}
       import org.apache.spark.sql.SparkSession

       object NotebookApplication {
         private var _args: Array[String] = null

         // Bootstrapping Spark Context
         protected lazy val _conf = new SparkConf().setAppName("Zeppelin Notebook Application")

         implicit lazy val spark: SparkSession = SparkSession.builder().config(_conf).getOrCreate()
         implicit lazy val sc: SparkContext = spark.sparkContext
         implicit lazy val sqlContext: SQLContext = spark.sqlContext

         // implicit lazy val sc: SparkContext = new SparkContext(_conf)
         // implicit lazy val sqlContext: SQLContext = new SQLContext(sc)

         import sqlContext.implicits._
         import sqlContext.sql
         import org.apache.spark.sql.functions._

         // extract case class declarations to outside
         %s

         final def main(args: Array[String]): Unit = {
           // Loading Class State
           this._args = args

           // Call Business Logic
           run()
         }

         // Analytics extracted from the notebook paragraphs
         def run() : Unit = {
            %s
         }
       }
    """
  def generateClass(notebook: Notebook, className: String): BatchSourceFile = {
    val parser = ParagraphParser
    val buffer = new StringBuilder
    var sqlCounter = 0

    for(p <- notebook.paragraphs) {
      if (p.text.trim.startsWith("%") == false) {
        buffer.append("// Paragraph " + p.id)
        buffer.append("\n")
        buffer.append(p.text)
        buffer.append("\n")
      } else if (p.text.trim.startsWith("%sql") &&
                 p.text.trim.contains("${") == false) { // we don't support parameterized queries
        val sqlQuery = p.text.
          substring("%sql".length).
          trim.
          replaceAll("\n", " ").
          replaceAll(char2Character(34).toString,
            char2Character(92).toString + char2Character(92).toString + char2Character(34).toString)

        println(sqlQuery) //scalastyle:ignore

        sqlCounter+=1
        buffer.append("\n")
        buffer.append("\n")
        buffer.append("// Paragraph " + p.id)
        buffer.append("\n")
        buffer.append("val sqlDF" + sqlCounter + " = spark.sql(\"" + sqlQuery + "\")")
        buffer.append("\n")
        buffer.append("sqlDF" + sqlCounter + ".show()")
      }
    }

    val source = buffer.toString
    val generatedSource = template.format(
        parser.getDeclarations(source),
        parser.getCleanSource(source))

    println(">>>" + generatedSource + "<<<") //scalastyle:ignore
    new BatchSourceFile(className, generatedSource)
  }
}
