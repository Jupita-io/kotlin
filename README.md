[![](https://jitpack.io/v/Jupita-io/kotlin.svg)](https://jitpack.io/#Jupita-io/kotlin)

# Jupita Kotlin SDK
This SDK is developed for Android using Kotlin and utilizes Google’s Volley library to create the API calls required. This library will allow you to make the required `dump` API calls with Jupita. All API calls are made asynchronously, thus there are event listeners available to handle the API results.


## Overview
Jupita is an API product that provides deep learning powered touchpoint analytics. Within the SDK documentation `message_type` refers to which user the utterance is from. `message_type` 0 = `TOUCHPOINT` and `message_type` 1 = `INPUT`, although these labels are handled by the SDK.

The required parameters for the APIs include setting `message_type` along with assigning a `touchpoint_id` + `input_id` to be passed. Please note when assigning the `touchpoint_id` that no data will be available for that particular touchpoint until the touchpoint has sent at least 1 utterance via the `dump` API. 

You may set any touchpoint or input ID format within the confines of JSON. How this is structured or deployed is completely customisable, for example, you may wish to use full names for users from your database, or you may wish to apply sequencing numbers for input users where the user is not known. Touchpoint & input IDs must be unique to that user. When dumping an initial touchpoint utterance where there is no input user, such as creating a new Twitter post, simply pass a nominal `input_id` each time, such as '0' for example.

## APIs
There is one API within the Jupita product – `dump`:

- `dump` allows you to dump each utterance.


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
	        implementation 'com.github.Jupita-io:kotlin:1.1.0'
	}
```

### Step 3
Build Jupita. Insert your API key as the token as well as a touchpoint user ID. In the example below '2' represents the touchpoint_id;

```
val token = "your-token"
val touchpoint_id = "2"
val touchpoint = Jupita.Builder(applicationContext, token, touchpoint_id).build()
```

### Step 4
Dump an utterance from a touchpoint by calling the dump API as a message by specifying the message text and the ID of the input, represented in the example below as '3'. 

The parameter `isCall` is required and set to false by default. This tells Jupita if the utterance is from an audio call. When dumping an utterance from an audio call, set the `isCall` parameter to `true` otherwise set to `false`;

```
touchpoint.dump("Hi, how are you?",
                "3",
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
```

Similarly, call the dump API whenever dumping an utterance from an input by specifying the message text and ID of the input;
```
touchpoint.dump("Hi, good thanks!",
                "3",
                Jupita.INPUT,
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
- `JSONException` which occurs if the user input is not JSON compatible. This can be incorrect usage of strings when passed on to the Jupita methods.
- `IllegalArgumentException` which occurs if the `message_type` set in the dump method is not 1 or 0.
- A 401 error code is thrown when the token is incorrect, otherwise Jupita returns error 400 with details.


## Libraries
Use Step 1 and 2 so that the Jupita Kotlin SDK is available within the scope of the project. Currently the Jupita Kotlin SDK is dependent on volley 1.2.1 and takes the permission of the web as soon as the SDK is added as a dependency.


## Classes
The available product under the Kotlin SDK is Jupita. Jupita can be constructed directly using the public constructor but it is highly recommended to use the Jupita.Builder class to build the product. This will ensure that mistakes are not made while building Jupita.

```
val token = "your-token"
val touchpoint_id = "2"
val touchpoint = Jupita.Builder(applicationContext, token, touchpoint_id).build()
```

The builder constructs with the context of the application, token, and the touchpoint_id. This is needed for building the volley request queue. The built touchpoint can now be used to call dump method asynchronously.

## `dump` method definitions

```
fun dump(text: String, input_id: String)
fun dump(text: String, input_id: String, dumpListener: DumpListener)
fun dump(text: String, input_id: String, message_type: Int, dumpListener: DumpListener)
fun dump(text: String, input_id: String, message_type: Int, isCall: Boolean, dumpListener: DumpListener)
```

If the values of `message_type` and `isCall` are not provided the values are considered as `Jupita.TOUCHPOINT` and `false` by default. Thus `text` and the `input_id` are essential when creating a `dump` request. To avoid illegal argument error use `Jupita.TOUCHPOINT` or `Jupita.INPUT` for `message_type`.

`DumpListener` is an interface which needs to be implemented to listen to results of the dump call. The onSuccess event returns the success message as well as the utterance rating as double.

## Support
If you require additional support please contact support@jupita.io
