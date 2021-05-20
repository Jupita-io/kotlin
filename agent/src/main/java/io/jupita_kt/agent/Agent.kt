package io.jupita_kt.agent

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.Exception

class Agent(
    context: Context,
    private val apiKey: String,
    private val agentId: String
    ){

    private val requestQueue = Volley.newRequestQueue(context)
    private fun dumpRequestAPI(text: String, clientId: String, type: Int, isCall: Boolean, dumpListener: DumpListener?){
        val jsonData = JSONObject(mapOf(
            "token" to apiKey,
            "agent id" to agentId,
            "client id" to clientId,
            "message type" to type,
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
            var body = ""
            var jsonResponse = JSONObject()
            val statusCode = it.networkResponse.statusCode.toString()

            if (it.networkResponse.data != null){
                try {
                    body = it.networkResponse.data.decodeToString()
                    jsonResponse = JSONObject(body)
                } catch (e: UnsupportedEncodingException){
                    Log.e(TAG, e.message, e)
                } catch (e: JSONException){
                    Log.e(TAG, e.message, e)
                }
            }

            dumpListener?.onError(statusCode, jsonResponse)
        }

        requestQueue.add(request)
    }

    fun dump(text: String, clientId: String, type: Int, isCall: Boolean, dumpListener: DumpListener){
        dumpRequestAPI(text, clientId, type, isCall, dumpListener)
    }

    // builder pattern
    class Builder(private val context: Context, private val apiKey: String){
        var agentId: String? = null

        fun build(): Agent{
            return agentId?.let { Agent(context, apiKey, it) } ?: throw Exception()
        }
    }

    // listeners
    interface DumpListener{
        fun onSuccess(msg: String, rating: Double)
        fun onError(statusCode: String, response: JSONObject)
    }

    companion object {
        private const val TAG = "AGENT"
    }
}