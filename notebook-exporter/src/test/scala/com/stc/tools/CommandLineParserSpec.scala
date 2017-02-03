/*
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
 */
package com.stc.tools

import com.stc.tools.notebook.CommandLineParser
import org.scalatest._


class CommandLineParserSpec extends FlatSpec with Matchers {

  it should "properly parse type commandline option" in {
    val expected = "zeppelin"

    val parameters = new CommandLineParser(
      s"--type=$expected" :: Nil
    )

    val actual = parameters.get("type")

    println(actual) //scalastyle:ignore

    assert(actual.toString.equals(expected))
  }


  it should "properly parse export commandline option" in {
    val expected = List("--export", "notebook.json", "--to", "application.jar")

    val parameters = new CommandLineParser(
      expected
    )

    val export = parameters.get("export")
    val to = parameters.get("to")

    println(export) //scalastyle:ignore
    println(to) //scalastyle:ignore

    export should be ("notebook.json")
    to should be ("application.jar")
  }
}
