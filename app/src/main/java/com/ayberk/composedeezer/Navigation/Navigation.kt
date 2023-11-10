package com.ayberk.composedeezer.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayberk.composedeezer.Anasayfa
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
    }
}