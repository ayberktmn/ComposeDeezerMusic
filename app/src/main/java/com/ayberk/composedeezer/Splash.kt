package com.ayberk.composedeezer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun Splash(navHostController: NavHostController) {
    LaunchedEffect(key1 = true) {
        // 3 saniye bekle
        delay(3000)
        // Anasayfaya geçiş yap
        navHostController.navigate("anasayfa")
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(color = Color.Black) {
            Image(
                painter = painterResource(id = R.drawable.logodeezer),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}