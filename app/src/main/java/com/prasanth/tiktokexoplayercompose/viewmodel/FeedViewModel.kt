package com.prasanth.tiktokexoplayercompose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.prasanth.tiktokexoplayercompose.usecase.GetFeed
import com.prasanth.tiktokexoplayercompose.usecase.model.Entity
import com.prasanth.tiktokexoplayercompose.usecase.model.Feed
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject  constructor(val getFeed: GetFeed):ViewModel() {
  var feedLiveData = MutableLiveData<Entity<List<Feed>>>()
  fun getFeed():LiveData<Entity<List<Feed>>>{
    return getFeed.execute().asLiveData()
  }
}