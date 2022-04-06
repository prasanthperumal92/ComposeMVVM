package com.prasanth.tiktokexoplayercompose.util

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache


object ExoplayerCache {
     var simpleCache: SimpleCache?=null
    fun get(context:Context):SimpleCache {
        if (simpleCache==null) {
            val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            simpleCache =
                SimpleCache(context.getCacheDir(), leastRecentlyUsedCacheEvictor, ExoDatabaseProvider(context))
        }


        return simpleCache!!

    }
}