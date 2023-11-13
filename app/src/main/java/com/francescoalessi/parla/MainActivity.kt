package com.francescoalessi.parla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.francescoalessi.parla.api.TextGenerationService
import com.francescoalessi.parla.ui.character.CharacterScreen
import com.francescoalessi.parla.ui.character.EditCharacterScreen
import com.francescoalessi.parla.ui.chats.Home
import com.francescoalessi.parla.ui.conversation.ConversationDetailScreen
import com.francescoalessi.parla.ui.navigation.AppDestinations
import com.francescoalessi.parla.ui.settings.SettingsScreen
import com.francescoalessi.parla.ui.theme.ParlaTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var service: TextGenerationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val isSystemInDarkTheme = isSystemInDarkTheme()
            val colorScheme = MaterialTheme.colorScheme
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = colorScheme.surface.copy(alpha = 0.8F),
                    darkIcons = !isSystemInDarkTheme
                )
                systemUiController.setNavigationBarColor(
                    color = colorScheme.surface,
                    darkIcons = !isSystemInDarkTheme
                )
            }

            ParlaTheme {
                SagaiApp()
            }
        }
    }
}

@Composable
fun SagaiApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Home.name
    ) {
        composable(
            route = AppDestinations.Conversation.name+"/{conversationId}",
            arguments = listOf(
                    navArgument("conversationId") {
                        type = NavType.IntType
                    }
            )
        ) {
            ConversationDetailScreen(
                viewModel = hiltViewModel(),
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = AppDestinations.Home.name) {
            Home(
                viewModel = hiltViewModel(),
                navController = navController
            ) { conversationId ->
                navController.navigate(AppDestinations.Conversation.name+"/$conversationId")
            }
        }

        composable(route = AppDestinations.EditCharacter.name+"?characterId={characterId}",
            arguments = listOf(
                    navArgument("characterId") {
                        type = NavType.IntType
                        defaultValue = 0
                    })
        ) {
           EditCharacterScreen(
               onBackPressed = {
                   navController.navigateUp()
               },
               viewModel = hiltViewModel()
           )
        }

        composable(route = AppDestinations.Character.name) {
            CharacterScreen(
                navController,
                hiltViewModel(),
                onCharacterClicked = { characterId ->
                    navController.navigate(AppDestinations.EditCharacter.name+"?characterId=$characterId")
                },
                onNewCharacterClicked = {
                    navController.navigate(AppDestinations.EditCharacter.name)
                }
            )
        }

        composable(route = AppDestinations.Settings.name) {
            SettingsScreen(hiltViewModel(), navController)
        }
    }
}

