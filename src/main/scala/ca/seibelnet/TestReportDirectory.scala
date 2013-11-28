package ca.seibelnet

import java.io.{IOException, File}

class TestReportDirectory(val targetPath: String) {

  val file = new File(targetPath)

  def getAbsolutePath: String = {
    file.getAbsolutePath
  }

  def setupTestDirectory: Unit = {
    if (file.exists()) deleteDirectory(file)
    file.mkdirs()
  }

  private def deleteDirectory(dir: File): Unit = {
    val dirContents = dir.listFiles()

    dirContents foreach { file: File =>
      if (file.isDirectory) deleteDirectory(file)
      else file.delete()

      if (file.exists()) throw new IOException("Failed to delete test report file or directory: " + file.getAbsolutePath)
    }
  }
}
