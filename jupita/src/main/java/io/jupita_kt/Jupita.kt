package io.jupita_kt

import android.content.Context
import io.jupita_kt.network.IRequest
import io.jupita_kt.network.Requestor
import io.jupita_kt.network.listeners.DumpListener

class Jupita(private val requestor: IRequest) {

    companion object {
        const val TOUCHPOINT = 0
        const val INPUT = 1
    }

    fun dump(
        text: String,
        input_id: String,
        channel_type: String,
        message_type: Int,
        isCall: Boolean,
        dumpListener: DumpListener
    ) {
        requestor.dump(text, input_id, channel_type, message_type, isCall, dumpListener)
    }

    fun dump(text: String, input_id: String, channel_type: String) {
        requestor.dump(text, input_id, channel_type, TOUCHPOINT, false, null)
    }

    fun dump(text: String, input_id: String, channel_type: String, dumpListener: DumpListener) {
        requestor.dump(text, input_id, channel_type, TOUCHPOINT, false, dumpListener)
    }

    fun dump(text: String, input_id: String, channel_type: String, message_type: Int, dumpListener: DumpListener) {
        requestor.dump(text, input_id, channel_type, message_type, false, dumpListener)
    }

    // builder pattern
    class Builder(context: Context, apiKey: String, touchpoint_id: String) {

        var requestor: IRequest = Requestor(context, apiKey, touchpoint_id)

        fun build() = Jupita(requestor)

    }

}
