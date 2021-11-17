package com.webflux.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Spring Boot runner
 */
@SpringBootApplication
class WebFluxKotlinApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<WebFluxKotlinApplication>(*args)
        }
    }
}