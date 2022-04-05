package com.prasanth.tiktokexoplayercompose.usecase

import com.prasanth.tiktokexoplayercompose.repositary.FeedExceptionRepositaryTestImpl
import com.prasanth.tiktokexoplayercompose.usecase.model.Entity
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetFeedTest {

    @Test
    fun execute() {
        runBlocking {
            val execute = GetFeed(FeedExceptionRepositaryTestImpl()).execute()
            execute.collect(FlowCollector {
                if (it.status==Entity.FAILED){
                    it.exception.printStackTrace()
                    assert(it.exception.message.equals("hello"))
                }
            })
        }
    }



}