package ca.seibelnet

trait TestGroupWriterFactory {
  def createTestGroupWriter(groupClassName: String): TestGroupWriter
}
