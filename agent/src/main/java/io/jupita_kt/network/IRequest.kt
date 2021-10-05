package io.jupita_kt.network

import io.jupita_kt.network.listeners.DumpListener

interface IRequest {
    fun dump(text: String, client_id: String, type: Int, isCall: Boolean, dumpListener: DumpListener?)
}