package ca.seibelnet

import org.scalatest.{BeforeAndAfter, FunSuite}
import java.io.{PrintWriter, IOException, File}

class TestReportDirectoryTest extends FunSuite with BeforeAndAfter {
  var underTest: TestReportDirectory = _

  before {
    underTest = new TestReportDirectory("./test-reports")
  }

  test("given a path is specified when getting the absolute path then the correct path is returned") {
    assert(underTest.getAbsolutePath.endsWith("test-reports"))
  }

  test("given a directory does not exist when setting up the directory then it is created and empty") {
    val reportsDir = new File("./test-reports")

    if (reportsDir.exists()) {
      deleteDirectory(reportsDir)
    }

    underTest.setupTestDirectory
    assert(reportsDir.exists())
    assert(reportsDir.isDirectory)
    assert(reportsDir.listFiles().length == 0)

    deleteDirectory(reportsDir)
  }

  test("given a directory exists and it contains files when setting up the directory then it is emptied") {
    val reportsDir = new File("./test-reports")

    if (reportsDir.exists()) {
      deleteDirectory(reportsDir)
    }

    reportsDir.mkdirs()

    createTestFile(new File(reportsDir.getAbsolutePath + File.separator + "file1.xml"))
    createTestFile(new File(reportsDir.getAbsolutePath + File.separator + "file2.xml"))

    assert(reportsDir.listFiles().length == 2)
    underTest.setupTestDirectory
    assert(reportsDir.exists())
    assert(reportsDir.isDirectory)
    assert(reportsDir.listFiles().length == 0)

    deleteDirectory(reportsDir)
  }

  def deleteDirectory(dir: File): Unit = {
    val dirContents = dir.listFiles()

    dirContents foreach { file: File =>
        if (file.isDirectory) deleteDirectory(file)
        else file.delete()

        if (file.exists()) throw new IOException("Failed to delete test file or directory: " + file.getAbsolutePath)
    }
  }

  def createTestFile(file: File): Unit = {
    val writer = new PrintWriter(file)
    writer.write("Test content")
    writer.close()
  }
}
