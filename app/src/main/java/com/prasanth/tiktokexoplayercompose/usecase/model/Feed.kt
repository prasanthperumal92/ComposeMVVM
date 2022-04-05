package com.prasanth.tiktokexoplayercompose.usecase.model

import com.prasanth.tiktokexoplayercompose.data.model.Feed

class Feed() {
    lateinit var hls:String
    lateinit var preview:String
    var isPotrait = false
    var isFocused = false

constructor(feed: Feed) : this() {
    hls = feed.hls
    preview = feed.preview
    isPotrait = feed.is_portrait
}
}