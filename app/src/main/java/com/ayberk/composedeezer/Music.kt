package com.ayberk.composedeezer

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.size.PixelSize
import com.airbnb.lottie.LottieAnimationView
import com.ayberk.composedeezer.models.album.Album
import com.ayberk.composedeezer.util.Resource
import com.ayberk.composedeezer.viewmodel.GenreViewModel
import com.bumptech.glide.load.resource.gif.GifBitmapProvider
import com.bumptech.glide.load.resource.gif.GifDrawable

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

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            MusicItemDialogContent(navHostController = navHostController, viewModel = viewModel, music = music)
        }
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
                        //  navHostController.navigate("artistdetail/${music.album.id}")
                        showDialog = true
                    }
                    .fillMaxHeight(),

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

@SuppressLint("ResourceType")
@Composable
fun MusicItemDialogContent(navHostController: NavHostController, viewModel: GenreViewModel, music: com.ayberk.composedeezer.model.album.Data) {
    Box(
        modifier = Modifier
            .size(1000.dp, 500.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                IconButton(
                    onClick = { navHostController.navigateUp() },
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = Color.White)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = rememberImagePainter(data = "https://e-cdns-images.dzcdn.net/images/cover/" + music.md5_image + "/1000x1000-000000-80-0-0.jpg"),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(5.dp, Color.Black),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = music.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                            .clickable {
                                viewModel.togglePlayPause(music.preview)
                            }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                            .clickable {
                              //  viewModel.togglePlayPause(music.preview)
                            }
                    )

                    val context = LocalContext.current
                    Image(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                            .clickable {
                                    shareOnWhatsApp(context, music.link)
                            }
                    )
                }
            }
        }
    }
}

fun shareOnWhatsApp(context: Context, text: String) {
    val sendIntent = Intent("android.intent.action.SEND")
    sendIntent.putExtra(Intent.EXTRA_TEXT, text)
    sendIntent.type = "text/plain"
    sendIntent.`package` = "com.instagram" // WhatsApp'ı hedef uygulama olarak belirtiyoruz

    try {
        context.startActivity(sendIntent)
    } catch (ex: ActivityNotFoundException) {
        // WhatsApp yüklü değilse veya desteklenmiyorsa hata işlemleri buraya eklenir.
        Toast.makeText(context, "Instagram yüklü değil veya desteklenmiyor.", Toast.LENGTH_SHORT).show()
    }
}


