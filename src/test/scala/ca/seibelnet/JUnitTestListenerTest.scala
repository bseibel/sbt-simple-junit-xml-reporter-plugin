package ca.seibelnet

import org.scalatest.{BeforeAndAfter, FunSuite}

class JUnitTestListenerTest extends FunSuite with BeforeAndAfter {
	var underTest: JUnitTestListener = _

    before {
        underTest = new JUnitTestListener("test-target-path")
    }

    test("nothing") {
    }
}
