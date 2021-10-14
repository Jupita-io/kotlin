[![](https://jitpack.io/v/Jupita-io/Jupita-Kotlin.svg)](https://jitpack.io/#Jupita-io/Jupita-Kotlin)

# Jupita Kotlin SDK

This SDK is developed for Android using Kotlin and utilizes Google’s Volley library to create the API calls required. This library will allow you to make the required `dump` API calls with Jupita. All API calls are made asynchronously, thus there are event listeners available to handle the API results.

## Overview
Jupita is an API product that provides deep learning powered touchpoint analytics. Within the SDK documentation, `message_type` will simply refer to who is speaking. `message_type` 0 = `touchpoint`, and `message_type` 1 = `input`, although these labels are handled by the SDK.

The required parameters for the APIs include setting `message_type`, along with assigning an `touchpoint_id` + `input_id` to be passed - how this is structured or deployed is completely flexible and customizable. Please note when assigning the `touchpoint_id` that no data will be available for that particular touchpoint until the touchpoint has sent at least 1 utterance via the `dump` API. 

## APIs
There is one API within the Jupita product – `dump`:

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
	        implementation 'com.github.Jupita-io:Jupita-Kotlin:1.1.0'
	}
```

### Step 3
Build Jupita - '2' has been used to represent the touchpoint_id;

```
val token = "your-token"
val touchpoint_id = "2"
val touchpoint = Jupita.Builder(applicationContext, token, touchpoint_id).build()
```

### Step 4
Call the dump API as a message from touchpoint by specifying the message and input_id – represented as '3' below;

```
touchpoint.dump( "Hello",
            "3",
            MessageType.Touchpoint,
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

Similarly, call the dump API whenever input responds back to the same touchpoint by specifying the message and ID of the input;
```
touchpoint.dump(
                "Hi, how are you?",
                "3",
                MessageType.Input,
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
JSONException which occurs if the user input is not json compatible. This can be incorrect usage of strings when passed on to the Jupita methods.
IllegalArgumentException: this arises if the `message_type` set in the dump method is not 1 or 0.

## Error codes
Error codes thrown are 401 when the token is incorrect.

## Libraries
Use Step 1 and 2 so that the Jupita Android SDK is available within the scope of the project. Currently the Jupita Android SDK is dependent on volley 1.2.1 and takes the permission of the internet as soon as the SDK is added as a dependency.

## Classes
The available product under the Kotlin SDK is Jupita. The Jupita Kotlin version cannot be constructed directly using the public constructor. Use the Jupita.Builder class to build the product.

```
val token = "your-token"
val touchpoint_id = "2"
val touchpoint = Jupita.Builder(applicationContext, token, touchpoint_id).build()
```

The builder constructs with the context of the application, token, and the touchpoint_id. This is needed for building the volley request queue.

The built touchpoint can now be used to call dump method asynchronously.

### `dump` method definitions

```
fun dump(text: String, input_id: String)
fun dump(text: String, input_id: String, dumpListener: DumpListener)
fun dump(text: String, input_id: String, type: Int, dumpListener: DumpListener)
fun dump(text: String, input_id: String, type: Int, isCall: Boolean, dumpListener: DumpListener)
```
The parameter `isCall` is required and set to false within the SDK. This tells Jupita whether or not the utterance is from an audio call. When dumping an utterance from an audio call, set the `isCall` parameter to `true`.

If the values of `type` and `isCall` are not provided by default the values are considered as `MessageType.Touchpoint` and `false`. Thus `text` and the `input_id` are essential when creating a `dump` request. To avoid illegal argument error use `MessageType.Touchpoint` or `MessageType.Input` for type.

`DumpListener` is an interface which needs to be implemented to listen to results of the dump call. The onSuccess event returns the success message as well as the utterance rating as double.

If you require additional support just hit us up at support@jupita.io 
