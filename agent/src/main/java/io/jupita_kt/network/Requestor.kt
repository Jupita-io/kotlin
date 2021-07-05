package io.jupita_kt.network

import android.content.Context
import android.util.Log
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.jupita_kt.agent.Agent
import io.jupita_kt.agent.Constants
import io.jupita_kt.agent.MessageType
import io.jupita_kt.agent.ModelName
import io.jupita_kt.network.listeners.DumpListener
import io.jupita_kt.network.listeners.FeedListener
import io.jupita_kt.network.listeners.RatingListener
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.IllegalArgumentException

class Requestor(context: Context, private val apiKey: String, private val agentId: String): IRequest {
    companion object {
       const val TAG = "REQUESTOR"
    }
    private val requestQueue = Volley.newRequestQueue(context)

    override fun dump(text: String, clientId: String, type: Int, isCall: Boolean, dumpListener: DumpListener?){
        if (type !in arrayOf(MessageType.Agent, MessageType.Client)){
            throw IllegalArgumentException("Use either `MessageType.Agent` or `MessageType.Client` type")
        }

        val jsonData = JSONObject(mapOf(
            "token" to apiKey,
            "agent_id" to agentId,
            "client_id" to clientId,
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

    override fun feed(feedListener: FeedListener?){
        val jsonData = JSONObject(
            mapOf(
                "token" to apiKey,
                "agent_id" to agentId
            )
        )

        val request = JsonObjectRequest(
            Constants.feedEndpoint,
            jsonData,
            { it?.let { feedListener?.onSuccess(it) }}
        ) {
            val errorResponse = createErrorResponse(it)
            feedListener?.onError(errorResponse.statusCode, errorResponse.jsonResponse)
        }

        requestQueue.add(request)

    }

    override fun rating(model: String, ratingListener: RatingListener?){
        if (model != ModelName.JUPITAV1){
            throw IllegalArgumentException("Only Jupita v1 is supported")
        }

        val jsonData = JSONObject(
            mapOf(
                "token" to apiKey,
                "agent_id" to agentId,
                "model" to model
            )
        )

        val request = JsonObjectRequest(
            Constants.ratingEndpoint,
            jsonData,
            {ratingListener?.onSuccess(it.getDouble("rating"))}
        ) {
            val errorResponse = createErrorResponse(it)
            ratingListener?.onError(errorResponse.statusCode, errorResponse.jsonResponse)
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