package lesson1

import org.junit.Test
import org.junit.jupiter.api.Tag

class TaskTestsJava : AbstractTaskTests() {

    @Test
    @Tag("Easy")
    fun testSortTimes() {
        sortTimes { inputName, outputName -> JavaTasks.sortTimes(inputName, outputName) }
    }

    @Test
    @Tag("Normal")
    fun testSortAddresses() {
        sortAddresses { inputName, outputName -> JavaTasks.sortAddresses(inputName, outputName) }
    }

    @Test
    @Tag("Normal")
    fun testSortTemperatures() {
        sortTemperatures { inputName, outputName -> JavaTasks.sortTemperatures(inputName, outputName) }
    }

    @Test
    @Tag("Normal")
    fun testSortSequence() {
        sortSequence { inputName, outputName -> JavaTasks.sortSequence(inputName, outputName) }
    }

    @Test
    @Tag("Easy")
    fun testMergeArrays() {
        mergeArrays { first, second -> JavaTasks.mergeArrays(first, second) }
    }
}
