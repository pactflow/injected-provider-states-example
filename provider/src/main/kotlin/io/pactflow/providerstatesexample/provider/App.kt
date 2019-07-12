package io.pactflow.providerstatesexample.provider

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
  SpringApplication.run(App::class.java, *args)
}
