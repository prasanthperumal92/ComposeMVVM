package com.prasanth.tiktokexoplayercompose.data.repositary

import com.prasanth.tiktokexoplayercompose.data.model.FeedResponse
import com.prasanth.tiktokexoplayercompose.data.service.FeedService

import kotlin.Exception

class FeedRepositaryImpl(val feedService: FeedService):FeedRepositary {
    override suspend fun getFeed(): FeedResponse? {
        val feed = feedService.getFeed()
        if (feed.isSuccessful) {
            return feed.body()
        }
        throw Exception(feed.message())
    }
}