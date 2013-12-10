package ca.seibelnet

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

import sbt._
import sbt.testing._

class JUnitTestListenerTest extends FunSuite with BeforeAndAfter with MockitoSugar {
  var mockTestGroupWriterFactory: TestGroupWriterFactory = _
  var mockTestReportDirectory: TestReportDirectory = _
  var underTest: JUnitTestListener = _

  before {
    mockTestGroupWriterFactory = mock[TestGroupWriterFactory]
    mockTestReportDirectory = mock[TestReportDirectory]
    underTest = new JUnitTestListener(mockTestGroupWriterFactory, mockTestReportDirectory)
  }

  test("when class under test is created then the test report directory is set up") {
    verify(mockTestReportDirectory).setupTestDirectory
  }

  test("given no group has been started when a test event is recorded then nothing happens") {
    val mockTestEvent = createMockTestEvent("testClassName")
    underTest.testEvent(mockTestEvent)
    verifyNoMoreInteractions(mockTestGroupWriterFactory)
  }

  test("given no group has been started when a test group is ended and it has passed then nothing happens") {
    underTest.endGroup("testClassName", TestResult.Passed)
    verifyNoMoreInteractions(mockTestGroupWriterFactory)
  }

  test("given no group has been started when a test group is ended and it has an exception then nothing happens") {
    underTest.endGroup("testClassName", new Exception)
    verifyNoMoreInteractions(mockTestGroupWriterFactory)
  }

  test("given no group has been started when a group is started then a TestGroupWriter is created") {
    underTest.startGroup("testClassName")
    verify(mockTestGroupWriterFactory).createTestGroupWriter("testClassName")
    verifyNoMoreInteractions(mockTestGroupWriterFactory)
  }


  test("given a group has been started when a test event is recorded then it is added to the writer") {
    val mockTestGroupWriter = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName")).thenReturn(mockTestGroupWriter)

    underTest.startGroup("testClassName")

    val mockTestEvent = createMockTestEvent("testClassName")
    underTest.testEvent(mockTestEvent)

    verify(mockTestGroupWriter).addEvent(mockTestEvent)
    verifyNoMoreInteractions(mockTestGroupWriter)
  }

  test("given a group has been started when a test event with two details is recorded then it is added to the writer only once") {
    val mockTestGroupWriter = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName")).thenReturn(mockTestGroupWriter)

    underTest.startGroup("testClassName")

    val mockEvent1 = mock[Event]
    when(mockEvent1.fullyQualifiedName).thenReturn("testClassName")
    val mockEvent2 = mock[Event]
    when(mockEvent2.fullyQualifiedName).thenReturn("testClassName")
    val mockTestEvent = mock[TestEvent]
    when(mockTestEvent.detail).thenReturn(Seq(mockEvent1, mockEvent2))

    underTest.testEvent(mockTestEvent)

    verify(mockTestGroupWriter).addEvent(mockTestEvent)
    verifyNoMoreInteractions(mockTestGroupWriter)
  }

  test("given a group has been started when the same group is ended then the writer writes") {
    val mockTestGroupWriter = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName")).thenReturn(mockTestGroupWriter)

    underTest.startGroup("testClassName")
    underTest.endGroup("testClassName", TestResult.Passed)

    verify(mockTestGroupWriter).write(mockTestReportDirectory)
    verifyNoMoreInteractions(mockTestGroupWriter)
  }

  test("given that two groups have been started when the first group is ended then the first writer writes") {
    val mockTestGroupWriter1 = mock[TestGroupWriter]
    val mockTestGroupWriter2 = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName1")).thenReturn(mockTestGroupWriter1)
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName2")).thenReturn(mockTestGroupWriter2)

    underTest.startGroup("testClassName1")
    underTest.startGroup("testClassName2")
    underTest.endGroup("testClassName1", TestResult.Passed)

    verify(mockTestGroupWriter1).write(mockTestReportDirectory)
    verifyNoMoreInteractions(mockTestGroupWriter1)
    verifyNoMoreInteractions(mockTestGroupWriter2)
  }

  test("given that two groups have been started when a test event is added to the first group then it is added to the first writer") {
    val mockTestGroupWriter1 = mock[TestGroupWriter]
    val mockTestGroupWriter2 = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName1")).thenReturn(mockTestGroupWriter1)
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName2")).thenReturn(mockTestGroupWriter2)

    underTest.startGroup("testClassName1")
    underTest.startGroup("testClassName2")

    val mockTestEvent = createMockTestEvent("testClassName1")
    underTest.testEvent(mockTestEvent)

    verify(mockTestGroupWriter1).addEvent(mockTestEvent)
    verifyNoMoreInteractions(mockTestGroupWriter1)
    verifyNoMoreInteractions(mockTestGroupWriter2)
  }

  test("given that two groups have been started when the second group is ended then the second writer writes") {
    val mockTestGroupWriter1 = mock[TestGroupWriter]
    val mockTestGroupWriter2 = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName1")).thenReturn(mockTestGroupWriter1)
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName2")).thenReturn(mockTestGroupWriter2)

    underTest.startGroup("testClassName1")
    underTest.startGroup("testClassName2")
    underTest.endGroup("testClassName2", TestResult.Passed)

    verify(mockTestGroupWriter2).write(mockTestReportDirectory)
    verifyNoMoreInteractions(mockTestGroupWriter1)
    verifyNoMoreInteractions(mockTestGroupWriter2)
  }

  test("given that two groups have been started when a test event is added to the second group then it is added to the second writer") {
    val mockTestGroupWriter1 = mock[TestGroupWriter]
    val mockTestGroupWriter2 = mock[TestGroupWriter]
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName1")).thenReturn(mockTestGroupWriter1)
    when(mockTestGroupWriterFactory.createTestGroupWriter("testClassName2")).thenReturn(mockTestGroupWriter2)

    underTest.startGroup("testClassName1")
    underTest.startGroup("testClassName2")

    val mockTestEvent = createMockTestEvent("testClassName2")
    underTest.testEvent(mockTestEvent)

    verify(mockTestGroupWriter2).addEvent(mockTestEvent)
    verifyNoMoreInteractions(mockTestGroupWriter1)
    verifyNoMoreInteractions(mockTestGroupWriter2)
  }

  def createMockTestEvent(className: String): TestEvent = {
    val mockEvent = mock[Event]
    when(mockEvent.fullyQualifiedName).thenReturn(className)
    val mockTestEvent = mock[TestEvent]
    when(mockTestEvent.detail).thenReturn(Seq(mockEvent))
    mockTestEvent
  }
}
