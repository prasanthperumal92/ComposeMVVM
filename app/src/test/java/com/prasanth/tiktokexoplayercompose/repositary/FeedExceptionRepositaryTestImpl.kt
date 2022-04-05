package com.prasanth.tiktokexoplayercompose.repositary

import com.prasanth.tiktokexoplayercompose.data.model.FeedResponse
import com.prasanth.tiktokexoplayercompose.data.repositary.FeedRepositary


class FeedExceptionRepositaryTestImpl: FeedRepositary {
    override suspend fun getFeed(): FeedResponse? {
        throw Exception("hello")
    }

}