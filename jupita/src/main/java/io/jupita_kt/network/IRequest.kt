package io.jupita_kt.network

import io.jupita_kt.network.listeners.DumpListener

interface IRequest {

    fun dump(
        text: String,
        input_id: String,
        channel_type: String,
        type: Int,
        isCall: Boolean,
        dumpListener: DumpListener?
    )

}
