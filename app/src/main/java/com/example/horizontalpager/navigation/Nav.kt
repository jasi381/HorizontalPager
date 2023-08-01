package com.example.horizontalpager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.horizontalpager.FirstScreen
import com.example.horizontalpager.TabLayout
import com.example.horizontalpager.screens.Screens

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController =navController , startDestination = Screens.SplashScreen.route){
        composable(Screens.SplashScreen.route){
            FirstScreen(navController = navController)
        }
        composable(Screens.HomeScreen.route){
            TabLayout()
        }

    }

}