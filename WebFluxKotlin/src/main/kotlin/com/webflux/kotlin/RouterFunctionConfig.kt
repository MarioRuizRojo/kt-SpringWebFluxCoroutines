package com.webflux.kotlin

import com.webflux.kotlin.handler.NumbersGroupHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.coRouter

/**
 *
 * @author Mario Ruiz Rojo
 *  <br>
 *  REST service Routing Configuration
 *  <br>
 *  Only one Endpoint:
 *  POST /api/equation/			It executes the equation
 */
@Configuration
class RouterFunctionConfig {

    @Value("\${url.this.service}")
    private val urlThisService: String = ""

    /**
     * Routing function, it distributes calls to endpoints
     */
    @FlowPreview
    @Bean
    fun routes(numbersGroupHandler:NumbersGroupHandler) = coRouter {
                POST(urlThisService,numbersGroupHandler::equation)
    }
}
