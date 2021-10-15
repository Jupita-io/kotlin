package io.jupita.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.jupita_kt.Jupita
import io.jupita_kt.network.listeners.DumpListener
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "JUPITA_DEMO"
    }

    private val token = BuildConfig.JUPITA_API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val touchpoint = Jupita.Builder(this, token, "demo").build()

            Log.d(TAG, "Start Dump Request")
            touchpoint.dump(
                "Hello",
                "0",
                Jupita.TOUCHPOINT,
                false,
                object : DumpListener {
                    override fun onSuccess(msg: String, rating: Double) {
                        Log.d(TAG, "onSuccess: message -> $msg")
                        Log.d(TAG, "onSuccess: rating -> $rating")
                    }

                    override fun onError(statusCode: String, response: JSONObject) {
                        Log.d(TAG, "onError: message -> $response")
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error Occurred")
            e.printStackTrace()
        }
    }

}