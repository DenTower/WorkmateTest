package com.example.workmatetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.workmatetest.presentation.MainScreen
import com.example.workmatetest.presentation.UserViewModel
import com.example.workmatetest.presentation.UsersScreen
import com.example.workmatetest.ui.theme.WorkmateTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkmateTestTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main_graph"
                ) {
                    navigation(startDestination = "main_screen", route = "main_graph") {
                        composable("main_screen") { backStackEntry ->
                            val viewModel: UserViewModel = hiltViewModel(backStackEntry)

                            MainScreen(
                                onNavigate = navController::navigate,
                                viewModel = viewModel
                            )
                        }

                        composable("users_screen") { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry("main_screen")
                            }
                            val viewModel: UserViewModel = hiltViewModel(parentEntry)

                            UsersScreen(
                                onNavigateUp = { navController.navigateUp() },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}