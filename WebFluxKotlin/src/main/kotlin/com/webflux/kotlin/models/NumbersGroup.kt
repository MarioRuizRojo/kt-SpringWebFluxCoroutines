package com.webflux.kotlin.models

/**
 *
 * @author Mario Ruiz Rojo
 * It represents the number variables inside the equation
 */
class NumbersGroup {

    /**
     * List of variables of the equation,
     * sorted by position in the array
     */
    private var numbersG: Array<Int?> = arrayOf()

    /**
     *
     * @return List of numbers
     */
    fun getNumbersG(): Array<Int?> {
        return numbersG
    }

    /**
     *
     * @param numbersG set to the list of numbers
     */
    fun setNumbersG(numbersG: Array<Int?>) {
        this.numbersG = numbersG
    }
}