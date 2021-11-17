package com.webflux.kotlin.handler

import com.webflux.kotlin.models.NumbersGroup
import com.webflux.kotlin.models.ResultString
import com.webflux.kotlin.services.MathsService
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Function


/**
 *
 * @author Mario Ruiz Rojo
 * It handles the REST calls to equation and calls to client simple maths to execute The Equation
 */
@Component
class NumbersGroupHandler {

    @Value("\${url.this.service}")
    private val urlThisService: String? = null

    /**
     * Client simple maths REST service
     */
    @Autowired
    private val mathsService: MathsService? = null

    private fun printInfoList(numberList: Array<Int?>) {
        for (i in numberList) print("$i-")
        println()
    }

    /**
     * Equation to execute:((n0+n1)*(n2+n3))+n4/(n5+n6)
     * @return the final result as Integer inside the JSON
     */
    @FlowPreview
    suspend fun equation(request: ServerRequest): ServerResponse {
        println("trying good function")
        var result : ResultString = ResultString()
        val monoNumbersGroup: Mono<NumbersGroup> = request.bodyToMono(NumbersGroup::class.java)
        try {
            var numbersGroupDef : Deferred<NumbersGroup> = GlobalScope.async {monoNumbersGroup.awaitSingleOrNull()!!}
            val numbersGroup = numbersGroupDef.await()
            val numberList : Array<Int?> = numbersGroup.getNumbersG()
            printInfoList(numbersGroup.getNumbersG())

            var firstAdditionDef : Deferred<Int> = GlobalScope.async {mathsService!!.add(numberList[0]!!,numberList[1]!!)!!.awaitSingleOrNull()!!}
            val firstAddition : Int = firstAdditionDef.await()
            var secondAdditionDef : Deferred<Int> = GlobalScope.async {mathsService!!.add(numberList[2]!!,numberList[3]!!)!!.awaitSingleOrNull()!!}
            val secondAddition : Int = secondAdditionDef.await()
            var multiplicationDef : Deferred<Int> = GlobalScope.async {mathsService!!.multiply(firstAddition,secondAddition)!!.awaitSingleOrNull()!!}
            val multiplication : Int = multiplicationDef.await()
            var thirdAdditionDef : Deferred<Int> = GlobalScope.async {mathsService!!.add(multiplication,numberList[4]!!)!!.awaitSingleOrNull()!!}
            val thirdAddition : Int = thirdAdditionDef.await()
            var fourthAdditionDef : Deferred<Int> = GlobalScope.async {mathsService!!.add(numberList[5]!!,numberList[6]!!)!!.awaitSingleOrNull()!!}
            val fourthAddition : Int = fourthAdditionDef.await()
            var divisionDef : Deferred<Int> = GlobalScope.async {mathsService!!.divide(thirdAddition,fourthAddition)!!.awaitSingleOrNull()!!}
            val division : Int = divisionDef.await()

            result.set(division.toString())
            return ServerResponse.created(URI.create(urlThisService))
                .contentType(MediaType.APPLICATION_JSON)
                .json().bodyAndAwait(result)
        } catch (e: Exception) {
            println("The Error: " + e.message)
            result.set(e.message!!)
            e.printStackTrace()
            return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .json().bodyAndAwait(result)
        }
    }

    /*
    JAVA Equivalent function, bad way coding, difficult reading
    public Mono<ServerResponse> equationBad(ServerRequest request){
		Mono<NumbersGroup> monoNumbersGroup = request.bodyToMono(NumbersGroup.class);
		return monoNumbersGroup
				.flatMap(numbersGroup -> {
					Integer[] numberList = numbersGroup.getNumbersG();
					printInfoList(numberList);
					return mathsService.add(numberList[0],numberList[1])
							.flatMap( firstAddition -> mathsService.add(numberList[2],numberList[3])
								.flatMap( secondAddition -> mathsService.multiply(firstAddition,secondAddition))
								.flatMap( multiplication -> mathsService.add(multiplication,numberList[4]))
								.flatMap( thirdAddition  -> mathsService.add(numberList[5],numberList[6])
									.flatMap( fourthAddition -> mathsService.divide(thirdAddition,fourthAddition))
									.flatMap( finalResult -> ServerResponse.created(URI.create(urlThisService))
										.contentType(MediaType.TEXT_PLAIN)
										.body(Mono.just(String.valueOf(finalResult)), String.class)
									)
								)
							);
				})
				.switchIfEmpty(ServerResponse.notFound().build())
				.onErrorResume(t->responseErrors(t));
	}
     */

    /**
     * Same Equation Bad Implementation, kotlin
     * @return the final result as Integer inside the JSON
     */
    fun equationBad(request: ServerRequest): Mono<ServerResponse>? {
        val monoNumbersGroup = request.bodyToMono(NumbersGroup::class.java)
        return monoNumbersGroup
            .flatMap { numbersGroup: NumbersGroup ->
                val numberList = numbersGroup.getNumbersG()
                printInfoList(numberList)
                mathsService!!.add(numberList[0]!!, numberList[1]!!)!!
                    .flatMap<ServerResponse>( { firstAddition: Int? ->
                            mathsService.add(
                                numberList[2]!!, numberList[3]!!
                            )!!
                                .flatMap<Int?>({ secondAddition: Int? ->
                                    mathsService.multiply(
                                        firstAddition!!, secondAddition!!
                                    )
                                })
                                .flatMap { multiplication: Int? ->
                                    mathsService.add(
                                        multiplication!!, numberList[4]!!
                                    )
                                }
                                .flatMap { thirdAddition: Int? ->
                                    mathsService.add(
                                        numberList[5]!!, numberList[6]!!
                                    )!!
                                        .flatMap<Int?>({ fourthAddition: Int? ->
                                            mathsService.divide(
                                                thirdAddition!!, fourthAddition!!
                                            )
                                        })
                                        .flatMap { finalResult: Int? ->
                                            ServerResponse.created(URI.create(urlThisService))
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .body(
                                                    Mono.just(finalResult.toString()),
                                                    String::class.java
                                                )
                                        }
                                }
                        }
                    )
            }
            .switchIfEmpty(ServerResponse.notFound().build())
            .onErrorResume(Function<Throwable, Mono<out ServerResponse>> { t: Throwable? ->
                ServerResponse.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(BodyInserters.fromValue(t!!.message!!))
            })
    }
}