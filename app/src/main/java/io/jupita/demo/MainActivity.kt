package io.jupita.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.jupita_kt.agent.Agent
import io.jupita_kt.agent.MessageType
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val token = BuildConfig.JUPITA_API_KEY
    companion object {
        private val TAG = "JUPITA_DEMO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val agent = Agent.Builder(this, token).apply {
                agentId = "demo"
            }.build()

            Log.d(TAG, "Start Dump Request")
            agent.dump(
                "Hello",
                "0",
                MessageType.Agent,
                false,
                object : Agent.DumpListener {
                    override fun onSuccess(msg: String, rating: Double) {
                        Log.d(TAG, "onSuccess: message -> $msg")
                        Log.d(TAG, "onSuccess: rating -> $rating")
                    }

                    override fun onError(statusCode: String, response: JSONObject) {
                        Log.d(TAG, "onError: message -> $response")
                    }
                }
            )

            Log.d(TAG, "Start Feed Request")
            agent.feed(object : Agent.FeedListener {
                override fun onSuccess(week: JSONObject) {
                    Log.d(TAG, "onSuccess: week -> $week")
                }

                override fun onError(statusCode: String, response: JSONObject) {
                    Log.d(TAG, "onError: message -> $response")
                }
            })

            Log.d(TAG, "Start Rating Request")
            agent.rating(object : Agent.RatingListener {
                override fun onSuccess(rating: Double) {
                    Log.d(TAG, "onSuccess: rating -> $rating")
                }

                override fun onError(statusCode: String, response: JSONObject) {
                    Log.d(TAG, "onError: message -> $response")
                }
            })
        } catch (e: Exception){
            Log.e(TAG, "Error Occured")
            e.printStackTrace()
        }
    }
}