package com.prasanth.tiktokexoplayercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.prasanth.tiktokexoplayercompose.ui.theme.TiktokExoplayerComposeTheme
import com.prasanth.tiktokexoplayercompose.usecase.model.Entity
import com.prasanth.tiktokexoplayercompose.usecase.model.Feed
import com.prasanth.tiktokexoplayercompose.util.ExoplayerCache
import com.prasanth.tiktokexoplayercompose.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TiktokExoplayerComposeTheme {


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MainActivityScreen()
                }
            }
        }


    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainActivityScreen(feedViewModel:FeedViewModel =viewModel()) {
    Scaffold(topBar = {
        TopAppBar {
            Text(text = "HLSPlayerList", fontFamily = FontFamily.SansSerif, color = Color.White)
        }
    }) {
        val pageState = rememberPagerState(pageCount = 2)
        val entityState = feedViewModel.getFeed().observeAsState()
        Column(modifier = Modifier.fillMaxSize()) {

           TabRow(selectedTabIndex = pageState.currentPage) {
               Tab(selected = false, onClick = {
                   runBlocking { pageState.scrollToPage(0)}
               }, text = {Text(text = "Explore")})
               Tab(selected = false, onClick = {
                   runBlocking { pageState.scrollToPage(1)}
               }, text = {Text(text = "My Videos")})
           }
           HorizontalPager(state = pageState,modifier= Modifier.fillMaxSize()) {page->
               if (page==pageState.currentPage) {
                   tabView(entityState)
               }
           }
       }
    }
}

@Composable
fun tabView(entity:State<Entity<List<Feed>>?>)
{
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (entity.value?.status==Entity.LOADING) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(30.dp)
                    .align(alignment = Alignment.Center)
            )
        }else if (entity.value?.status==Entity.DATA_AVAILABLE){

            Column() {
                LazyColumn (state = listState){
                    if (entity.value!!.response!=null) {
                        for (i in 0 until entity.value!!.response?.size!!)
                        {
                           
                            item {
                                getItem(entity.value!!.response?.get(i)!!,i, listState)
                            }
                        }
                    }
                }

            }
        }
    }

}

@Composable
private fun getItem(entry: Feed,position: Int,listState:LazyListState) {
    Column(modifier = Modifier.padding(10.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "profile_pic",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
        )
        Spacer(modifier = Modifier.size(5.dp))
        if (position!=listState.firstVisibleItemIndex) {
            Box{
                AsyncImage(
                model = entry.preview,
                contentDescription = entry.preview,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black),
                contentScale = ContentScale.Fit

            )
                Image(painter = painterResource(id = R.drawable.ic_baseline_play_circle_filled_24),
                    contentDescription = "play_icon", modifier = Modifier
                        .align(
                            Alignment.Center
                        )
                        .size(30.dp)
                        .clickable { runBlocking { listState.scrollToItem(position) } }
                        )

            }
        }else{
            MyPlayer(feed = entry, isFocused = position==listState.firstVisibleItemIndex)
        }
    }
}



@Composable
fun MyPlayer(feed: Feed,isFocused:Boolean) {
    val sampleVideo = feed.hls
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()
    val playerView = StyledPlayerView(context)
    val mediaItem = MediaItem.fromUri(sampleVideo)
    val playWhenReady = isFocused
    val httpDataSourceFactory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    val defaultDataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(context)
    val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(ExoplayerCache.get(context = context))
        .setUpstreamDataSourceFactory(defaultDataSourceFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    val mediaSource: MediaSource = HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)


    player.setMediaSource(mediaSource)
    playerView.player = player
    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady

    }
    DisposableEffect(playerView) {
        onDispose {
            playerView.player = null
            player.release()
        }
    }

    AndroidView(factory = {
        playerView
    }, modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .onFocusChanged {
            if (!it.hasFocus) {
                player.pause()
            }
        }){

    }
}

