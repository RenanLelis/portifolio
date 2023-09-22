package com.renan.webstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebstoreApplication

fun main(args: Array<String>) {
	runApplication<WebstoreApplication>(*args)
}
