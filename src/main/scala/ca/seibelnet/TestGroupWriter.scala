package ca.seibelnet

import sbt.TestEvent

trait TestGroupWriter {
  def addEvent(testEvent: TestEvent)
  def write(reportDirectory: TestReportDirectory)
}
