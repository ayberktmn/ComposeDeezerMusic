package com.ayberk.composedeezer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.ayberk.composedeezer.model.album.Data
import com.ayberk.composedeezer.model.artistdetail.ArtistDetail
import com.ayberk.composedeezer.models.album.Album
import com.ayberk.composedeezer.models.album.Contributor
import com.ayberk.composedeezer.util.Resource
import com.ayberk.composedeezer.viewmodel.GenreViewModel

@Composable
fun ArtistDetail(navHostController:NavHostController,artist_id:Int,viewModel: GenreViewModel = hiltViewModel()) {

    val artistdetailItem = produceState<Resource<ArtistDetail>>(initialValue = Resource.Loading()){
        value = viewModel.LoadArtistDetail(artist_id)
    }.value

    val albumItem = produceState<Resource<Album>>(initialValue = Resource.Loading()){
        value = viewModel.LoadAlbum(artist_id)
    }.value

    (arrayListOf(artistdetailItem.data) ?: emptyList())?.let { ArtistDetailList(navHostController, artistdetail = it) }

    Spacer(modifier = Modifier.height(30.dp))

    AlbumList(navHostController = navHostController, albumList = (albumItem.data?.data ?: emptyList()))
}

@Composable
fun ArtistDetailList(navHostController: NavHostController, artistdetail: List<ArtistDetail?>){
    ArtistDetailItemGrid(navHostController = navHostController,artistdetail)
}

@Composable
fun ArtistDetailItemGrid(navHostController: NavHostController, artistdetail: List<ArtistDetail?>){
    LazyVerticalGrid(GridCells.Fixed(1)){
        items(artistdetail) { artistdetail ->
            if (artistdetail != null) {
                ArtistDetailItem(navHostController = navHostController, artistdetail = artistdetail)
            }
        }
    }
}


@Composable
fun ArtistDetailItem(navHostController: NavHostController, artistdetail: ArtistDetail) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Kart içeriği buraya eklenecek
            Image(
                painter = rememberImagePainter(data = artistdetail.picture_xl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //     navHostController.navigate("artistdetail/${artistdetail.id}")
                    }
                    .height(100.dp) // Yüksekliği yarıya indiriliyor
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
                Spacer(modifier = Modifier.weight(0.9f))
                Image(
                    painter = rememberImagePainter(data = artistdetail.picture),
                    contentDescription = null,
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    text = artistdetail.name,
                    style = MaterialTheme.typography.headlineSmall,
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

@Composable
fun AlbumItem(navHostController: NavHostController, album: Data) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickable {
                navHostController.navigate("music/${album.album.id}")
            }
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .clickable {
                    navHostController.navigate("music/${album.album.id}")
                    println("music/${album.album.id}")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = album.contributors[0].picture_xl),
                contentDescription = null,
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .padding(8.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Sütunun genişliğini tamamen doldur ve içeriden boşluk bırak
                horizontalAlignment = Alignment.CenterHorizontally, // Sütun içindeki öğeleri yatayda ortala

            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(2.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun AlbumList(navHostController: NavHostController, albumList: List<Data?>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp) // Sayfanın üst kısmına padding ekleniyor
    ) {
        AlbumItemGrid(navHostController = navHostController, albumList)
    }
}

@Composable
fun AlbumItemGrid(navHostController: NavHostController, albumList: List<Data?>) {
    LazyVerticalGrid(GridCells.Fixed(2)) {
        items(albumList) { album ->
            if (album != null) {
                AlbumItem(navHostController = navHostController, album = album)
            }
        }
    }
}



