package com.prasanth.tiktokexoplayercompose.usecase

import com.prasanth.tiktokexoplayercompose.data.repositary.FeedRepositary
import com.prasanth.tiktokexoplayercompose.usecase.model.Entity
import com.prasanth.tiktokexoplayercompose.usecase.model.Feed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

public class GetFeed @Inject constructor(val feedRepositary: FeedRepositary) {

     fun execute(): Flow<Entity<List<Feed>>> {
        return flow {
            try {
                emit(Entity())
                val feed = feedRepositary.getFeed()
                val list = feed?.data?.list
                val map = list?.map { Feed(it) }

                emit(Entity.success(map!!))
            } catch (e: Exception) {
                emit(Entity.failure(e))
            }
        }
    }




}