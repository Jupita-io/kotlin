package io.jupita_kt.Jupita

import android.content.Context
import io.jupita_kt.network.IRequest
import io.jupita_kt.network.Requestor
import io.jupita_kt.network.listeners.DumpListener

class Touchpoint(private val requestor: IRequest) {


    fun dump(
        text: String,
        input_id: String,
        type: Int,
        isCall: Boolean,
        dumpListener: DumpListener
    ) {
        requestor.dump(text, input_id, type, isCall, dumpListener)
    }

    fun dump(text: String, input_id: String) {
        requestor.dump(text, input_id, MessageType.Touchpoint, false, null)
    }

    fun dump(text: String, input_id: String, dumpListener: DumpListener) {
        requestor.dump(text, input_id, MessageType.Touchpoint, false, dumpListener)
    }

    fun dump(text: String, input_id: String, type: Int, dumpListener: DumpListener) {
        requestor.dump(text, input_id, type, false, dumpListener)
    }

    // builder pattern
    class Builder(context: Context, apiKey: String, touchpoint_id: String) {
        var requestor: IRequest = Requestor(context, apiKey, touchpoint_id)
        fun build() = Touchpoint(requestor)
    }
}