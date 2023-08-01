package com.example.horizontalpager.screens

sealed class Screens{
    object SplashScreen: Screens(){
        const val route = "splash_screen"
    }
    object HomeScreen: Screens(){
        const val route = "home_screen"
    }

}
