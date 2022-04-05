package com.prasanth.tiktokexoplayercompose.data.repositary

import com.prasanth.tiktokexoplayercompose.data.model.FeedResponse
import com.prasanth.tiktokexoplayercompose.data.service.FeedService
import retrofit2.Response
import javax.inject.Inject

interface FeedRepositary {
    suspend fun getFeed():FeedResponse?
}