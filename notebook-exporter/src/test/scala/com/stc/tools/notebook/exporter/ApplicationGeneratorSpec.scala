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

import java.nio.file.Paths

import org.scalatest.FlatSpec

class ApplicationGeneratorSpec  extends FlatSpec {

  "generated code" should "contain paragraph source code" in {

    val targetDirectory = Paths.get("./target/scala-2.11/generated-classes")
    val notebookResource = getClass().getResource("/notebooks/zeppelin/scala-tutorial.json")
    val notebook = ZeppelinNotebook(notebookResource.getPath)
    val generated = ApplicationGenerator.generateClass(notebook, "NotebookApplication.class")

    val source = new String(generated.content)

    assert(source.contains("Paragraph 20150210-015259_1403135953"))
  }
}