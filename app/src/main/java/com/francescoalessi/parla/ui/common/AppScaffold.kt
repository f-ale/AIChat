package com.francescoalessi.parla.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.francescoalessi.parla.R
import com.francescoalessi.parla.ui.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavController,
                topBar: @Composable () -> Unit = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_launcher_foreground),
                                    "",
                                    modifier = Modifier.size(84.dp),
                                )
                                //Text(stringResource(R.string.app_name))
                            } },
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