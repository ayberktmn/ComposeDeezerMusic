package com.ayberk.composedeezer


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.util.Resource
import com.ayberk.composedeezer.viewmodel.GenreViewModel


@Composable
fun Artist(navHostController:NavHostController,genre_id:Int,viewModel:GenreViewModel= hiltViewModel()) {

    val genreItem = produceState<Resource<Artist>>(initialValue = Resource.Loading()){
        value = viewModel.LoadArtist(genre_id)
    }.value

    ArtistList(navHostController, artist = genreItem.data?.data ?: emptyList())
}


@Composable
fun ArtistList(navHostController: NavHostController,artist: List<com.ayberk.composedeezer.model.artist.Data>){
    ArtistItemGrid(navHostController = navHostController,artist )
}


@Composable
fun ArtistItemGrid(navHostController: NavHostController,artist: List<com.ayberk.composedeezer.model.artist.Data>){
    LazyVerticalGrid(GridCells.Fixed(2)){
        items(artist) { artist ->
            ArtistItem(navHostController = navHostController, artist = artist)
        }
    }
}


@Composable
fun ArtistItem(navHostController: NavHostController, artist: com.ayberk.composedeezer.model.artist.Data) {

    Card(
        modifier = Modifier
            .size(300.dp, 150.dp)
            .padding(8.dp)
            .clickable {
                //  navHostController.navigate("artist/${artist.id}")
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box {
            Image(
                painter = rememberImagePainter(data = artist.picture_xl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navHostController.navigate("artistdetail")
                    }
                    .height(75.dp) // Yüksekliği yarıya indiriliyor
                    .graphicsLayer(
                        clip = true,
                        translationY = (-37.5).toFloat() // Yüksekliğin yarısını alacak şekilde ayarlandı
                    ),
                contentScale = ContentScale.Crop // Resmin içeriğini kırp
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.7f))
                Image(
                    painter = rememberImagePainter(data = artist.picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    text = artist.name,
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

