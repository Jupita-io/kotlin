[![](https://jitpack.io/v/Jupita-io/Jupita-Agent-Android-Kotlin.svg)](https://jitpack.io/#Jupita-io/Jupita-Agent-Android-Kotlin)

# Jupita Agent Kotlin SDK

This SDK is developed for Android using Kotlin and utilizes Google’s Volley library to create the API calls required. This library will allow you to make the required `dump` API calls with Jupita Agent. All API calls are made asynchronously, thus there are event listeners available to handle the API results.

## Overview
Jupita Agent is an API product that provides deep learning powered communications analytics. Within the SDK documentation, `message_type` will simply refer to who is speaking. `message_type` 0 = `agent`, and `message_type` 1 = `client`, although these labels are handled by the SDK.

The required parameters for the APIs include setting `message_type`, along with assigning an `agent_id` + `client_id` to be passed - how this is structured or deployed is completely flexible and customizable. Please note when assigning the `agent_id` that no data will be available for that particular agent until the agent has sent at least 1 utterance via the `dump` API. 

## APIs
There is one API within the Jupita Agent product – `dump`:

- `Dump` allows you to dump each communication utterance.

## Quickstart
### Step 1
Add the following code under root build.gradle;

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2
Add the dependency in the applications build.gradle file;

```
dependencies {
	        implementation 'com.github.Jupita-io:Jupita-Agent-Android-Kotlin:1.1.0'
	}
```

### Step 3
Build Jupita Agent - '2' has been used to represent the agent_id;

```
val token = "your-token"
val agent_id = "2"
val agent = Agent.Builder(applicationContext, token, agent_id).build()
```

### Step 4
Call the dump API as a message from Agent by specifying the message and client_id – represented as '3' below;

```
agent.dump( "Hello",
            "3",
            MessageType.Agent,
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
```

Similarly, call the dump API whenever client responds back to the same agent by specifying the message and ID of the client;
```
agent.dump(
                "Hello",
                "3",
                MessageType.Client,
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
```

## Error handling
The SDK throws 2 errors:
JSONException which occurs if the user input is not json compatible. This can be incorrect usage of strings when passed on to the Agent methods.
IllegalArgumentException: this arises if the `message_type` set in the dump method is not 1 or 0, or the model name in rating method is not ‘JupitaV1’.

## Error codes
Error codes thrown are 401 when the token is incorrect and 400 when there is an attempt to dump gibberish content to the server, although the model does have an inbuilt gibberish detector.

## Libraries
Use Step 1 and 2 so that the Jupita Agent Android SDK is available within the scope of the project. Currently the Jupita Agent Android SDK is dependent on volley 1.2.0 and takes the permission of the internet as soon as the SDK is added as a dependency.

## Classes
The available product under the Android SDK is Jupita Agent.
The Jupita Agent Kotlin version cannot be constructed directly using the public constructor. Use the Agent.Builder class to build the product.

```
val token = "your-token"
val agent_id = "2"
val agent = Agent.Builder(applicationContext, token, agent_id).build()
```

The builder constructs with the context of the application, token, and the agent_id. This is needed for building the volley request queue.

The built agent can now be used to call dump method asynchronously.

### `dump` method definitions

```
fun dump(text: String, client_id: String)
fun dump(text: String, client_id: String, dumpListener: DumpListener)
fun dump(text: String, client_id: String, type: Int, dumpListener: DumpListener)
fun dump(text: String, client_id: String, type: Int, isCall: Boolean, dumpListener: DumpListener)
```

If the values of `type` and `isCall` are not provided by default the values are considered as `MessageType.Agent` and `false`. Thus `text` and the `client_id` are essential when creating a `dump` request. To avoid illegal argument error use `MessageType.Agent` or `MessageType.Client` for type.

`DumpListener` is an interface which needs to be implemented to listen to results of the dump call.
The onSuccess event returns the success message as well as the utterance rating as double.
