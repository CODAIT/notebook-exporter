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

import java.io.{File, OutputStream}

import joptsimple.util.KeyValuePair
import joptsimple.{OptionParser, OptionSet, OptionSpec}

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._



class CommandLineParser(args: Seq[String]) {
  private val parser = new OptionParser()
  parser.allowsUnrecognizedOptions()

  /*
   * Configure parser with supported options
   */
  private val help =
    parser.acceptsAll(Seq("help", "h").asJava, "display help information").forHelp()

  private val version =
    parser.acceptsAll(Seq("version", "v").asJava, "display version information")

  private val notebookType =
    parser.accepts("type", "identify the Notebook type (e.g. Zeppelin, Jupyther)")
        .withOptionalArg()
        .ofType(classOf[String])
        .defaultsTo("zeppelin")

  private val export =
    parser.accepts("export", "export notebook as a Spark Application")
      .withRequiredArg()
      .ofType(classOf[String])

  private val destination =
    parser.accepts("to", "export notebook as a Spark Application")
      .withRequiredArg()
      .ofType(classOf[String])

  /*
   * Parse provided configuration options
   */
  private val options = parser.parse(args.map(_.trim): _*)

  def has(key: String): Boolean = {
    return options.has(key)
  }

  def get(key: String): String = {
    return options.valueOf(key).asInstanceOf[String]
  }

  def getAll(key: String): List[String] = {
    return  scala.collection.JavaConversions.asScalaBuffer(options.valuesOf(key)).
      toList.asInstanceOf[List[String]]
  }

  def has[T](spec: OptionSpec[T]): Boolean =
    options.has(spec)

  def get[T](spec: OptionSpec[T]): Option[T] =
    Some(options.valueOf(spec)).filter(_ != null)

  def getAll[T](spec: OptionSpec[T]): Option[List[T]] =
    Some(options.valuesOf(spec).asScala.toList).filter(_ != null)


  /**
    * Prints the help message to the output stream provided.
    *
    * @param out The output stream to direct the help message
    */
  def printHelpOn(out: OutputStream): Unit =
    parser.printHelpOn(out)
}
