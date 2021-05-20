package io.jupita_kt.network.listeners

import org.json.JSONObject

interface RatingListener {
    fun onSuccess(rating: Double)
    fun onError(statusCode: String, response: JSONObject)
}