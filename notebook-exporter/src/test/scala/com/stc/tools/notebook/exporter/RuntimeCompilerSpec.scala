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
package com.stc.tools.notebook.exporter

import java.io.File
import java.nio.file.{Files, Path, Paths}

import org.scalatest.FlatSpec

import scala.reflect.internal.util.{BatchSourceFile, SourceFile}
import scala.reflect.io.VirtualDirectory
import com.stc.utils.FileUtils

class RuntimeCompilerSpec extends FlatSpec {

  it should "compile a valid generated scala class resource" in {
    val className = "NotebookApplication.scala"

    val sourceFilePath: Path =
      Paths.get(getClass().getResource("/templates/NotebookApplication.scala").toURI)

    val sourceFile = new BatchSourceFile(
      className,
      new String(Files.readAllBytes(sourceFilePath))
      )

    val targetDirectory = new VirtualDirectory("(test)", None)

    val compiler = new RuntimeCompiler(targetDirectory)
    compiler.compile(sourceFile)

    assert(targetDirectory.size > 0)
  }
}
