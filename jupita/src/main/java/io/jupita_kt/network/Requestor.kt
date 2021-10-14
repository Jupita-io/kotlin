package io.jupita_kt.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.jupita_kt.Constants
import io.jupita_kt.MessageType
import io.jupita_kt.network.listeners.DumpListener
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class Requestor(context: Context, private val apiKey: String, private val touchpoint_id: String) :
    IRequest {

    companion object {
        const val TAG = "REQUESTOR"
    }

    private val requestQueue = Volley.newRequestQueue(context)

    override fun dump(
        text: String,
        input_id: String,
        type: Int,
        isCall: Boolean,
        dumpListener: DumpListener?
    ) {
        if (type !in arrayOf(MessageType.Touchpoint, MessageType.Input)) {
            throw IllegalArgumentException("Use either `MessageType.Touchpoint` or `MessageType.Input` type")
        }

        val jsonData = JSONObject(
            mapOf(
                "token" to apiKey,
                "touchpoint_id" to touchpoint_id,
                "input_id" to input_id,
                "message_type" to type,
                "text" to text,
                "isCall" to isCall
            )
        )

        val request = JsonObjectRequest(
            Request.Method.POST,
            Constants.dumpEndpoint,
            jsonData,
            {
                try {
                    dumpListener?.onSuccess(
                        it.getString("message"),
                        it.getDouble("score")
                    )
                } catch (e: JSONException) {
                    Log.e(TAG, e.message, e)
                    e.printStackTrace()
                }
            }
        ) {
            val errorResponse = createErrorResponse(it)
            dumpListener?.onError(errorResponse.statusCode, errorResponse.jsonResponse)
        }

        requestQueue.add(request)
    }

    private fun createErrorResponse(errorResponse: VolleyError): ErrorResponse {
        val body: String
        var jsonResponse = JSONObject()
        val statusCode = errorResponse.networkResponse.statusCode.toString()

        if (errorResponse.networkResponse.data != null) {
            try {
                body = errorResponse.networkResponse.data.decodeToString()
                jsonResponse = JSONObject(body)
            } catch (e: UnsupportedEncodingException) {
                Log.e(TAG, e.message, e)
            } catch (e: JSONException) {
                Log.e(TAG, e.message, e)
            }
        }
        return ErrorResponse(jsonResponse, statusCode)
    }

}