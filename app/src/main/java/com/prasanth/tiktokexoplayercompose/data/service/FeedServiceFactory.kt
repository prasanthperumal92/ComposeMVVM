package com.prasanth.tiktokexoplayercompose.data.service

object FeedServiceFactory {
    fun getInstance():FeedService{
     return   ServiceManager.getInstance(ServiceUrl.BASE_URL).create(FeedService::class.java)
    }
}