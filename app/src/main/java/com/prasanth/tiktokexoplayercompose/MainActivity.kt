package com.prasanth.tiktokexoplayercompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.prasanth.tiktokexoplayercompose.ui.theme.TiktokExoplayerComposeTheme
import com.prasanth.tiktokexoplayercompose.usecase.model.Entity
import com.prasanth.tiktokexoplayercompose.usecase.model.Feed
import com.prasanth.tiktokexoplayercompose.viewmodel.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

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
               tabView(entityState)
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
                        for (i in 0..entity.value!!.response?.size!!)
                        {
                           
                            item {
                                getItem(entity.value!!.response?.get(i)!!,i==listState.firstVisibleItemIndex)
                            }
                        }
                    }
                }

            }
        }
    }

}

@Composable
private fun getItem(entry: Feed,is_focused: Boolean) {

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
        if (!is_focused) {
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
                        )

            }
        }else{
            MyPlayer(feed = entry, isFocused = is_focused)
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
    player.setMediaItem(mediaItem)
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
        .height(300.dp),){

    }
}

