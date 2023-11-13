package com.francescoalessi.parla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
            ),
            enterTransition = {
                slideInVertically { -it }
            },
            exitTransition = {
                slideOutVertically { -it }
            }
        ) {
            ConversationDetailScreen(
                viewModel = hiltViewModel(),
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = AppDestinations.Home.name,
            enterTransition = {
                when (initialState.destination.route) {
                    AppDestinations.Settings.name, AppDestinations.Character.name ->
                        slideInHorizontally(
                            initialOffsetX = { fullWidth ->
                                -fullWidth
                            }
                        )

                    else -> fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppDestinations.Settings.name, AppDestinations.Character.name ->
                        slideOutHorizontally { -it }

                    else -> fadeOut()
                }
            }
        ) {
            Home(
                viewModel = hiltViewModel(),
                navController = navController
            ) { conversationId ->
                navController.navigate(AppDestinations.Conversation.name+"/$conversationId")
            }
        }

        composable(
            route = AppDestinations.EditCharacter.name+"?characterId={characterId}",
            arguments = listOf(
                    navArgument("characterId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }),
            enterTransition = {
                slideInVertically { -it } // TODO: Choose better transition
            },
            exitTransition = {
                slideOutVertically { -it }
            }
        ) {
           EditCharacterScreen(
               onBackPressed = {
                   navController.navigateUp()
               },
               viewModel = hiltViewModel()
           )
        }

        composable(
            route = AppDestinations.Character.name,
            enterTransition = {
                when (initialState.destination.route) {
                    AppDestinations.Home.name ->
                        slideInHorizontally(
                            initialOffsetX = { fullWidth ->
                                fullWidth
                            }
                        )

                    AppDestinations.Settings.name ->
                        slideInHorizontally(
                            initialOffsetX = { fullWidth ->
                                -fullWidth
                            }
                        )

                    else -> fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppDestinations.Settings.name ->
                        slideOutHorizontally { -it }

                    AppDestinations.Home.name ->
                        slideOutHorizontally { it }


                    else -> fadeOut()
                }
            }
        ) {
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

        composable(
            route = AppDestinations.Settings.name,
            enterTransition = {
                when (initialState.destination.route) {
                    AppDestinations.Home.name, AppDestinations.Character.name ->
                        slideInHorizontally(
                            initialOffsetX = { fullWidth ->
                                fullWidth
                            }
                        )

                    else -> fadeIn()
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppDestinations.Home.name, AppDestinations.Character.name ->
                        slideOutHorizontally { it }

                    else -> fadeOut()
                }
            }
        ) {
            SettingsScreen(hiltViewModel(), navController)
        }
    }
}

