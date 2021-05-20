package io.jupita_kt.network

import io.jupita_kt.network.listeners.DumpListener
import io.jupita_kt.network.listeners.FeedListener
import io.jupita_kt.network.listeners.RatingListener

interface IRequest {
    fun dump(text: String, clientId: String, type: Int, isCall: Boolean, dumpListener: DumpListener?)
    fun feed(feedListener: FeedListener?)
    fun rating(model: String, ratingListener: RatingListener?)
}