package com.ayberk.composedeezer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.ayberk.composedeezer.model.Genre.Data
import com.ayberk.composedeezer.viewmodel.GenreViewModel


@Composable
fun Anasayfa(navHostController : NavHostController,genreviewModel: GenreViewModel = hiltViewModel()) {
    var allowBackNavigation by remember { mutableStateOf(true) }
    BackHandler(enabled = allowBackNavigation){}
    GenreList(navHostController)
}


@Composable
fun GenreList(navHostController: NavHostController, viewModel: GenreViewModel = hiltViewModel()) {

    val genreList by remember { viewModel.genreList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    // Arka plan rengini değiştirmek için
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        GenreItemGrid(navHostController = navHostController, category = genreList)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }
    }
}


@Composable
fun GenreItemGrid(navHostController: NavHostController,category: List<Data>){
    LazyVerticalGrid(GridCells.Fixed(2)){
        items(category) { category ->
            CategoryItem(navHostController = navHostController, category = category)
        }
    }
}

@Composable
fun CategoryItem(navHostController: NavHostController, category: Data) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                // Kategoriye tıklandığında ara bir sayfaya yönlendir
                navHostController.navigate("artist/${category.id}")
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                // Kategoriye tıklandığında ara bir sayfaya yönlendir
                                navHostController.navigate("artist/${category.id}")
                            },
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Image(
                            painter = rememberImagePainter(data = category.picture),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                                .padding(start = 3.dp)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.music),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Top)
                        )
                    }
                }
            }
        }
    }
}
