package io.jupita_kt.agent

import android.content.Context
import io.jupita_kt.network.IRequest
import io.jupita_kt.network.Requestor
import io.jupita_kt.network.listeners.DumpListener

class Agent(private val requestor: IRequest) {


    fun dump(
        text: String,
        client_id: String,
        type: Int,
        isCall: Boolean,
        dumpListener: DumpListener
    ) {
        requestor.dump(text, client_id, type, isCall, dumpListener)
    }

    fun dump(text: String, client_id: String) {
        requestor.dump(text, client_id, MessageType.Agent, false, null)
    }

    fun dump(text: String, client_id: String, dumpListener: DumpListener) {
        requestor.dump(text, client_id, MessageType.Agent, false, dumpListener)
    }

    fun dump(text: String, client_id: String, type: Int, dumpListener: DumpListener) {
        requestor.dump(text, client_id, type, false, dumpListener)
    }

    // builder pattern
    class Builder(context: Context, apiKey: String, agent_id: String) {
        var requestor: IRequest = Requestor(context, apiKey, agent_id)
        fun build() = Agent(requestor)
    }
}