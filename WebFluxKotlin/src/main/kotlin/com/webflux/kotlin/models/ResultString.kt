package com.webflux.kotlin.models

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class ResultString : Flow<Any> {
    private var res : String = ""

    public fun get() = res
    public fun set(value:String) { res = value }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Any>) {
        collector.emit(this.res)
    }
}