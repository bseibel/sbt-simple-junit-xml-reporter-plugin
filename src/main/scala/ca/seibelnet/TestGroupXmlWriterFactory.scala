package ca.seibelnet

// TODO Test this!
class TestGroupXmlWriterFactory extends TestGroupWriterFactory {
  def createTestGroupWriter(groupClassName: String): TestGroupWriter = {
    new TestGroupXmlWriter(groupClassName)
  }
}
