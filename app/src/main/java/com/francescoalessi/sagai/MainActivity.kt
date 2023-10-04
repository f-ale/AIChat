package com.francescoalessi.sagai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.ui.character.CharacterScreen
import com.francescoalessi.sagai.ui.character.EditCharacterScreen
import com.francescoalessi.sagai.ui.conversation.ConversationDetailScreen
import com.francescoalessi.sagai.ui.conversation.ConversationViewModel
import com.francescoalessi.sagai.ui.home.Home
import com.francescoalessi.sagai.ui.navigation.AppDestinations
import com.francescoalessi.sagai.ui.settings.SettingsScreen
import com.francescoalessi.sagai.ui.theme.SagaiAIChatTheme
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

            SagaiAIChatTheme {
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

