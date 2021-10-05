package io.jupita_kt.network

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.jupita_kt.agent.Constants
import io.jupita_kt.agent.MessageType
import io.jupita_kt.agent.ModelName
import io.jupita_kt.network.listeners.DumpListener
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.IllegalArgumentException

class Requestor(context: Context, private val apiKey: String, private val agent_id: String): IRequest {
    companion object {
       const val TAG = "REQUESTOR"
    }
    private val requestQueue = Volley.newRequestQueue(context)

    override fun dump(text: String, client_id: String, type: Int, isCall: Boolean, dumpListener: DumpListener?){
        if (type !in arrayOf(MessageType.Agent, MessageType.Client)){
            throw IllegalArgumentException("Use either `MessageType.Agent` or `MessageType.Client` type")
        }

        val jsonData = JSONObject(mapOf(
            "token" to apiKey,
            "agent_id" to agent_id,
            "client_id" to client_id,
            "message_type" to type,
            "text" to text,
            "isCall" to isCall
        ))

        val request = JsonObjectRequest(
            Constants.dumpEndpoint,
            jsonData,
            {
                try{
                    dumpListener?.onSuccess(
                        it.getString("message"),
                        it.getDouble("score")
                    )
                } catch (e: JSONException){
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