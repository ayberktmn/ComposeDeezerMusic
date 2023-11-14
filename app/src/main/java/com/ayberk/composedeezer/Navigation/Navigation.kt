package com.ayberk.composedeezer.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ayberk.composedeezer.Anasayfa
import com.ayberk.composedeezer.Artist
import com.ayberk.composedeezer.ArtistDetail
import com.ayberk.composedeezer.Login
import com.ayberk.composedeezer.Register
import com.ayberk.composedeezer.Splash

@Composable
fun Navigation() {

    val navHostController = rememberNavController()

    NavHost(navHostController, startDestination = "splash") {
        composable("splash") {
            Splash(navHostController)
        }
        composable("login") {
            Login(navHostController)
        }
        composable("anasayfa") {
            Anasayfa(navHostController)
        }
        composable("register") {
            Register(navHostController)
        }
        composable("artist/{genre_id}",
            arguments = listOf(
                navArgument("genre_id") {
                    type = NavType.IntType
                }
            )
        ) {
            val genreId = remember {
                it.arguments?.getInt("genre_id") ?: 0
            }
            Artist(
                genre_id = genreId,
                navHostController = navHostController
            )
        }
        composable("artistdetail/{artist_id}",
            arguments = listOf(
                navArgument("artist_id"){
                    type = NavType.IntType
                }
            )
        ) {
            val artistId = remember {
                it.arguments?.getInt("artist_id") ?: 0
            }
            ArtistDetail( artist_id = artistId,
                navHostController = navHostController)
        }
    }
}