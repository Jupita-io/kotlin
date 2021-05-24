[![](https://jitpack.io/v/Jupita-io/Jupita-Agent-Android-Kotlin.svg)](https://jitpack.io/#Jupita-io/Jupita-Agent-Android-Kotlin)

# Jupita Agent Kotlin SDK

This SDK is developed for Android using Kotlin and utilizes Google’s Volley library to create the API calls required.
Currently, the Android SDK fully supports the 3 APIs available for Jupita Agent.
All API calls are made asynchronously, thus there are event listeners available to handle the API results.

## APIs

There are 3 APIs within the Juptia Agent product – `dump` `rating` & `feed`:
- Dump allows you to send the utterances you wish to send.
- Rating allows you to retrieve the rating for the agent in question.
- Feed provides you with some basic rating analytics.

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
Add the dependency in the app’s build.gradle file;
```
dependencies {
	        implementation 'com.github.Jupita-io:Jupita-Agent-Android-Kotlin:0.1.1
	}
```

### Step 3
Build Jupita Agent - '2' has been used to represent the agentId;

```
val token = "your-token"
val agentId = "2"
val agent = Agent.Builder(applicationContext, token, agentId).build()
```

### Step 4
Call the dump API as a message from Agent by specifying the message and clientId – represented as '3' below;

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

### Step 5
Call the rating API;

```
agent.rating(object : RatingListener {
    override fun onSuccess(rating: Double) {
        Log.d(TAG, "onSuccess: rating -> $rating")
    }

    override fun onError(statusCode: String, response: JSONObject) {
        Log.d(TAG, "onError: message -> $response")
    }
})
```

### Step 6
Call the feed API;
```
agent.feed(object : FeedListener {
    override fun onSuccess(week: JSONObject) {
        Log.d(TAG, "onSuccess: week -> $week")
    }

    override fun onError(statusCode: String, response: JSONObject) {
        Log.d(TAG, "onError: message -> $response")
    }
})
```

## Error handling
The SDK throws 2 errors:
- JSONException which occurs if the user input is not json compatible. This can be incorrect usage of strings when passed on to the Agent methods.
- IllegalArgumentException: this arises if the message type set in the dump method is not 1 or 0, or the model name in rating method is not ‘JupitaV1’.

## Error codes
Error codes thrown are 401 when the token is incorrect and 400 when there is an attempt to dump gibberish content to the server, although the model does have an inbuilt gibberish detector.

## Libraries
Use Step 1 and 2 so that the Jupita Agent Android SDK is available within the scope of the project. Currently the Jupita Agent Android SDK is dependent on volley 1.2.0 and takes the permission of the internet as soon as the SDK is added as a dependency.

## Classes
The available product under the Android SDK is Jupita Agent.
The Jupita Agent Kotlin version cannot be constructed directly using the public constructor. Use the Agent.Builder class to build the product.

```
val token = "your-token"
val agentId = "2"
val agent = Agent.Builder(applicationContext, token, agentId).build()
```

The builder constructs with the context of the application, token, and the agentID. This is needed for building the volley request queue.

The built agent can now be used to call dump, rating and feed methods asynchronously.

### `dump` method definitions

```
fun dump(text: String, clientId: String)
fun dump(text: String, clientId: String, dumpListener: DumpListener)
fun dump(text: String, clientId: String, type: Int, dumpListener: DumpListener)
fun dump(text: String, clientId: String, type: Int, isCall: Boolean, dumpListener: DumpListener)
```

If the values of `type` and `isCall` are not provided by default the values are considered as `MessageType.Agent` and `false`.
Thus `text` and the `clientId` are essential when creating a `dump` request.
To avoid illegal argument error use `MessageType.Agent` or `MessageType.Client` for type.
`DumpListener` is an interface which needs to be implemented to listen to results of the dump call.
The onSuccess event returns the success message as well as the utterance rating as double.


### `rating` method definitions
```
fun rating(ratingListener: RatingListener)
fun rating(modelName: String, ratingListener: RatingListener)
```

The second rating definition is created for future use when there will be multiple models to choose from.
At the moment only 1 model (JupitaV1) is supported. To avoid illegal argument error use `ModelName.JUPITAV1` for the modelName.
RatingListener is an interface which needs to be implemented to listen to results of the rating call.
The onSuccess event returns the rating as a double.


### `feed` method definitions
```
fun feed(feedListener: FeedListener)
```

FeedListener is an interface which needs to be implemented to listen to results of the feed call.
The onSuccess event returns the feed for the whole week as a JSONObject.

