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
import java.nio.file.{Files, Paths}
import java.util

import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.reflect.io.Path


case class Notebook (name: String, id: String, paragraphs: List[Paragraph])
case class Paragraph (id: String, title: Option[String], text: String)


object ZeppelinNotebook {

  def apply(notebookPath: String): Notebook = parseNotebook(notebookPath)

  def apply(multipleNotebookPaths: List[String]): Notebook = ({
    var paragraphs: List[Paragraph] = List[Paragraph]()
    multipleNotebookPaths.foreach( n => {
      val tmpNotebook: Notebook = parseNotebook(n)
      paragraphs = paragraphs ::: tmpNotebook.paragraphs
    }
    )

    var notebook: Notebook = Notebook("Notebook Pipeline", "1", paragraphs)
    notebook
  })

  def parseNotebook(notebookPath: String): Notebook = ({
    if (Files.exists(Paths.get(notebookPath)) == false) {
      throw new IOException("Notebook does not exist: '" + notebookPath + "'")
    }

    implicit val formats = DefaultFormats

    val jsonNote = parse(FileInput(new java.io.File(notebookPath)))
    val notebook = jsonNote.extract[Notebook]

    notebook
  })
}


case class JypnbNotebook (cells: List[JypnbParagraph])
case class JypnbParagraph (cell_type: String, source: List[String])


object JupyterNotebook {

  def apply(notebookPath: String): Notebook = parseNotebook(notebookPath)


  def apply(multipleNotebookPaths: List[String]): Notebook = ({
    var paragraphs: List[Paragraph] = List[Paragraph]()
    multipleNotebookPaths.foreach( n => {
      val tmpNotebook: Notebook = parseNotebook(n)
      paragraphs = paragraphs ::: tmpNotebook.paragraphs
      }
    )

    var notebook: Notebook = Notebook("Notebook Pipeline", "1", paragraphs)
    notebook
  })


  private def parseNotebook(notebookPath: String): Notebook = ({
    if (Files.exists(Paths.get(notebookPath)) == false) {
      throw new IOException("Notebook does not exist: '" + notebookPath + "'")
    }

    implicit val formats = DefaultFormats

    val jsonNote = parse(FileInput(new java.io.File(notebookPath)))
    val jupyterNotebook = jsonNote.extract[JypnbNotebook]

    var id = 0
    var paragraphs: List[Paragraph] = List[Paragraph]()
    jupyterNotebook.cells.foreach(cell => {
      if (cell.cell_type.equalsIgnoreCase("code")) {
        id += 1
        var source = new StringBuilder
        cell.source.foreach( line => {
          source.append(line)
        })
        val paragraph = Paragraph(id.toString, None, source.toString() )
        paragraphs = paragraph :: paragraphs
      }
    })

    val notebookName = Path(notebookPath).name
    val notebook = Notebook(notebookName, "1", paragraphs)

    notebook
  })
}
