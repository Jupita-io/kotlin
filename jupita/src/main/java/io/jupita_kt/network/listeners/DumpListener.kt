package io.jupita_kt.network.listeners

import org.json.JSONObject

// listeners
interface DumpListener{
    fun onSuccess(msg: String, rating: Double)
    fun onError(statusCode: String, response: JSONObject)
}