package io.jupita_kt.network

import org.json.JSONObject

data class ErrorResponse(
    val jsonResponse: JSONObject,
    val statusCode: String
)