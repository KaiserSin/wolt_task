package project.wolt_test

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WoltTestApplication

fun main(args: Array<String>) {
	val logger = LoggerFactory.getLogger(WoltTestApplication::class.java)
	try {
		runApplication<WoltTestApplication>(*args)
		logger.info("Application started successfully.")
	} catch (e: Exception) {
		logger.error("Application failed to start: ${e.message}", e)
	}
}
