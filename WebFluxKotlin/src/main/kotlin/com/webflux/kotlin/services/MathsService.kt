package com.webflux.kotlin.services

import reactor.core.publisher.Mono

/**
 *
 * @author Mario Ruiz Rojo
 * Business layer interface for managing REST api simple math calculations
 */
interface MathsService {
    /**
     * GET Addition
     * @param i1
     * @param i2
     * @return value from maths/add/
     */
    fun add(i1: Int, i2: Int): Mono<Int?>?
    /**
     * GET Multiply
     * @param i1
     * @param i2
     * @return value from maths/multiply/
     */
    fun multiply(i1: Int, i2: Int): Mono<Int?>?
    /**
     * GET Division
     * @param divided
     * @param divider
     * @return value from maths/divide/
     */
    fun divide(divided: Int, divider: Int): Mono<Int?>?
}