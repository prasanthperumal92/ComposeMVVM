package com.prasanth.tiktokexoplayercompose.di

import com.prasanth.tiktokexoplayercompose.data.repositary.FeedRepositary
import com.prasanth.tiktokexoplayercompose.data.repositary.FeedRepositaryImpl
import com.prasanth.tiktokexoplayercompose.data.service.FeedService
import com.prasanth.tiktokexoplayercompose.data.service.FeedServiceFactory
import com.prasanth.tiktokexoplayercompose.data.service.ServiceManager
import com.prasanth.tiktokexoplayercompose.data.service.ServiceUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class Injector {


   @Provides
   @ViewModelScoped
   fun getFeedRepositary():FeedRepositary{
      return FeedRepositaryImpl(FeedServiceFactory.getInstance())
   }

}