package com.prasanth.tiktokexoplayercompose.data.model

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

class FeedResponse {
 lateinit var data:FeedList
}
class FeedList{
    lateinit var list:List<Feed>

}

class Feed{
    var id :Long = 0
    var user_id :Long = 0
    lateinit var hls:String
    lateinit var preview:String
    lateinit var title:String
    lateinit var updated_on:String
    var total_comments:Int=0
    var total_votes:Int=0
    var total_views:Int=0
    var total_likes:Int=0
    var is_portrait:Boolean = false
}