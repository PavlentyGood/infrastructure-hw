package com.stringconcat.people

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PeopleApplication

fun main(args: Array<String>) {
	SpringApplication.run(PeopleApplication::class.java, *args)
}
