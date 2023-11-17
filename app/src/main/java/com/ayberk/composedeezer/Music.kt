package com.ayberk.composedeezer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.ayberk.composedeezer.models.album.Album
import com.ayberk.composedeezer.util.Resource
import com.ayberk.composedeezer.viewmodel.GenreViewModel

@Composable
fun Music(navHostController: NavHostController, album_id:Int, viewModel: GenreViewModel = hiltViewModel()) {

    val musicItem = produceState<Resource<Album>>(initialValue = Resource.Loading()){
        value = viewModel.LoadMusic(album_id)
    }.value



    MusicList(navHostController, music = musicItem.data?.data ?: emptyList())
}


@Composable
fun MusicList(navHostController: NavHostController, music: List<com.ayberk.composedeezer.model.album.Data>){
    MusicItemGrid(navHostController = navHostController,music )
}


@Composable
fun MusicItemGrid(navHostController: NavHostController,music: List<com.ayberk.composedeezer.model.album.Data>){
    LazyVerticalGrid(GridCells.Fixed(2)){
        items(music) { music ->
            MusicItem(navHostController = navHostController, music = music)
        }
    }
}


@Composable
fun MusicItem(navHostController: NavHostController,viewModel: GenreViewModel = hiltViewModel(), music: com.ayberk.composedeezer.model.album.Data) {

    val isPlaying by viewModel.isPlaying.collectAsState()
    var imageResourceId by remember { mutableStateOf(R.drawable.play) }

    LaunchedEffect(isPlaying) {
        // Observe changes in isPlaying and update imageResourceId accordingly
        imageResourceId = if (isPlaying) R.drawable.pause else R.drawable.play
    }

    Card(
        modifier = Modifier
            .size(200.dp, 150.dp)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box {
            Image(
                painter = rememberImagePainter(data = "https://e-cdns-images.dzcdn.net/images/cover/" + music.md5_image + "/1000x1000-000000-80-0-0.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //  navHostController.navigate("artistdetail/${artist.id}")
                    }
                    .fillMaxHeight(), // Yüksekliği yarıya indiriliyor

                contentScale = ContentScale.FillBounds // Entire image fits inside the bounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.7f))
                Image(
                    painter = rememberImagePainter(R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable {
                            viewModel.togglePlayPause(music.preview)
                        }
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.weight(0.2f))

                Text(
                    text = music.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                        .padding(start = 3.dp)
                )
            }
        }
    }

}