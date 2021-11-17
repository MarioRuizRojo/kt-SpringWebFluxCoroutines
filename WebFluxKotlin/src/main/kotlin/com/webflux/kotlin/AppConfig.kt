package com.webflux.kotlin

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class AppConfig {

    @Value("\${url.microservice.maths}")
    private val urlMicroServiceMaths: String? = null


    @Bean
    fun registerWebClient(): WebClient? {
        return WebClient.create(urlMicroServiceMaths)
    }
}