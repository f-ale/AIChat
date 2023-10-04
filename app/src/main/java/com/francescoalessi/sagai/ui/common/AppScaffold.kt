package com.francescoalessi.sagai.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.francescoalessi.sagai.ui.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavController,
                topBar: @Composable () -> Unit = {
                    CenterAlignedTopAppBar(
                        title = { Text("Sagai AI Chat") },
                    )
                },
                floatingActionButton: @Composable () -> Unit = {},
                content: @Composable (paddingValues: PaddingValues) -> Unit) {
    Scaffold(
        topBar = topBar,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = floatingActionButton,
        content = content
    )
}