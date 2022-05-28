package info.offthecob.jvm.platform.logging

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spock.lang.Specification

class StructuredLoggingLayoutTest extends Specification {
    ByteArrayOutputStream buffer

    def setup() {
        buffer =  new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
    }

    def "happy path"() {
        given:
        def name = "DJ"

        when:
        ClassThatLogs.sayHi(name)
        def output =  new JsonSlurper().parseText(buffer.toString())
        output.remove("timestamp")

        then:
        output == [
                level: "INFO",
                service: "testing",
                logger: "info.offthecob.jvm.platform.logging.ClassThatLogs",
                thread: "Test worker",
                message: "Hi, ${name}!"
        ]
    }

    def "stacktrace"() {
        given:
        def message = "uh oh"

        when:
        ClassThatLogs.throwStack(message)
        def output = new JsonSlurper().parseText(buffer.toString())

        then:
        output["stacktrace"].first().contains("java.lang.RuntimeException")
        output["level"] == "ERROR"
        output["message"] == message
    }

    def "json"() {
        given:
        def someValue = "some value"
        def someOtherValue = "some other value"
        def myMessage = "my message"

        when:
        ClassThatLogs.mdc(StructuredLoggingLayout.JSON, JsonOutput.toJson([someKey: someValue, someOtherKey: someOtherValue]), myMessage)
        def output = new JsonSlurper().parseText(buffer.toString())

        then:
        output["someKey"] == someValue
        output["someOtherKey"] == someOtherValue
        output["message"] == myMessage
    }
}
