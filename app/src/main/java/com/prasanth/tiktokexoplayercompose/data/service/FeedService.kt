package com.prasanth.tiktokexoplayercompose.data.service

import com.prasanth.tiktokexoplayercompose.data.model.FeedResponse
import retrofit2.Response
import retrofit2.http.GET

interface FeedService:BaseService {
    @GET(ServiceUrl.FEED)
    abstract suspend fun getFeed() : Response<FeedResponse>


}