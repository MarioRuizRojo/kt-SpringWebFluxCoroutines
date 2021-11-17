package com.webflux.kotlin.services

import com.webflux.kotlin.models.ResultNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI

/**
 *
 * @author Mario Ruiz Rojo
 * Business layer interface for managing REST api simple math calculations
 */
@Service
class MathsServiceImpl : MathsService {

    @Value("\${param1name}")
    private val param1name: String? = null

    @Value("\${param2name}")
    private val param2name: String? = null

    @Value("\${url.add}")
    private val urlAdd: String? = null

    @Value("\${url.multiply}")
    private val urlMultiply: String? = null

    @Value("\${url.divide}")
    private val urlDivide: String? = null

    /**
     * REST api simple maths service
     */
    @Autowired
    private val client: WebClient? = null

    /**
     * It creates url with 2 params inside for the simple maths operations
     * @param url
     * @param i1
     * @param i2
     * @return value from maths/url
     */
    private fun createUri(url: String, i1: Int, i2: Int, uriBuilder: UriBuilder): URI? {
        return uriBuilder
            .path(url)
            .queryParam(param1name, "{$param1name}")
            .queryParam(param2name, "{$param2name}")
            .build(i1, i2)
    }

    /**
     * GENERIC GET to maths service, any operation
     * @param url
     * @param i1
     * @param i2
     * @return value from maths/url
     */
    fun generic(url: String, i1: Int, i2: Int): Mono<Int?>? {
        println("$url$i1-$i2")
        return client!!.get()
            .uri { uriBuilder: UriBuilder ->
                createUri(
                    url,
                    i1,
                    i2,
                    uriBuilder
                )
            }
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ResultNumber::class.java)
            .flatMap { resnum ->
                System.out.println("res:" + resnum.res)
                Mono.just(resnum.res)
            }
    }


    /**
     * GET Addition
     * @param i1
     * @param i2
     * @return value from maths/add/
     */
    override fun add(i1: Int, i2: Int): Mono<Int?>? {
        return generic(urlAdd!!, i1, i2)
    }

    /**
     * GET Multiply
     * @param i1
     * @param i2
     * @return value from maths/multiply/
     */
    override fun multiply(i1: Int, i2: Int): Mono<Int?>? {
        return generic(urlMultiply!!, i1, i2)
    }

    /**
     * GET Division
     * @param i1
     * @param i2
     * @return value from maths/divide/
     */
    override fun divide(i1: Int, i2: Int): Mono<Int?>? {
        return generic(urlDivide!!, i1, i2)
    }
}