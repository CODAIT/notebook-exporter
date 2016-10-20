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

import java.io.IOException

import org.scalatest.FlatSpec

class NotebookSpec extends FlatSpec {

  it should "parse a valid notebook" in {
    val notebookResource = getClass().getResource("/notebooks/zeppelin/scala-tutorial.json")
    val notebook = ZeppelinNotebook(notebookResource.getPath)

    assert(notebook.name ==  "Zeppelin Tutorial")
    assert(notebook.paragraphs.size > 0)
  }

  it should "produce IOException when invalid notebook is passed" in {
    assertThrows[IOException] {
      val notebook = ZeppelinNotebook("/notebooks/zeppelin/invalid.json")
    }
  }
}
