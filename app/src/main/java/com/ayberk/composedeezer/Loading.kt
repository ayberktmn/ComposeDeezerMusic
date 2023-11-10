package com.ayberk.composedeezer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun Loading() {
    var loadingCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Yükleme işlemi burada gerçekleşir
        // Örnek: Yükleme işlemi 1.5 saniye sürsün
        delay(1500)

        // Yükleme işlemi tamamlandığında göstergenin kapanmasını sağlar
        loadingCompleted = true
    }

    if (!loadingCompleted) {
        // Yükleme işlemi devam ediyorsa Loading göstergesini göster
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}