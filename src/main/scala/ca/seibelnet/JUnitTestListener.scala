package ca.seibelnet

import sbt._
import sbt.testing._
import Keys._

/**
 * User: bseibel
 * Date: 12-04-25
 * Time: 12:02 PM
 */

// TODO test this!
object JUnitTestReporting extends Plugin {
  override def settings = Seq(
    testListeners += new JUnitTestListener("./test-reports/", new TestGroupXmlWriterFactory)
  )
}

class JUnitTestListener(val targetPath: String, val writerFactory: TestGroupWriterFactory) extends TestReportListener {

  var currentOutput: Map[String, TestGroupWriter] = Map()

  def testEvent(event: TestEvent): Unit = {
    if (event.detail.size > 0) {
      currentOutput.get(event.detail.head.fullyQualifiedName) match {
          case Some(v) => v.addEvent(event)
          case None    => Unit
      }
    }
  }

  def endGroup(name: String, result: TestResult.Value): Unit = {
    flushOutput(name)
  }

  def endGroup(name: String, t: Throwable): Unit = {
    flushOutput(name)
  }

  def startGroup(name: String): Unit = {
    currentOutput = currentOutput + (name -> writerFactory.createTestGroupWriter(name))
  }

  private def flushOutput(name: String): Unit = {
    val file = new File(targetPath)
    file.mkdirs()

    currentOutput.get(name) match {
      case Some(v) =>  v.write(targetPath)
      case None    =>  Unit
    }
  }

}
