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
package com.stc.tools.utils

import java.io.IOException

import com.stc.tools.notebook.exporter.ZeppelinNotebook
import com.stc.utils.FileUtils
import org.scalatest.FlatSpec

class FileUtilsSpec extends FlatSpec {
  val testDirectory = getClass().getResource("/com/stc/tools/utils/directory/")

  it should "list files of a existing directory" in {
      val existingFiles = FileUtils.getListOfFiles(testDirectory.getPath)
      assert(existingFiles.size == 2)
  }


  it should "fail when provided directory is a file" in {
    assertThrows[IOException] {
      FileUtils.getListOfFiles("./com/stc/tools/utils/directory/resource.txt")
    }
  }

  it should "fail when provided directory is invalid" in {
    assertThrows[IOException] {
      FileUtils.getListOfFiles("./com/stc/tools/utils/invalid/")
    }
  }

  it should "only list files of specific extension" in {
    val existingFiles = FileUtils.getFilteredLisfOfFiles(testDirectory.getPath, "rtf")
    assert(existingFiles.size == 1)
  }
}
