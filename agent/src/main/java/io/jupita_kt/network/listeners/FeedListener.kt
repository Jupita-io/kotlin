package io.jupita_kt.network.listeners

import org.json.JSONObject

interface FeedListener {
    fun onSuccess(week: JSONObject)
    fun onError(statusCode: String, response: JSONObject)
}