package com.francescoalessi.sagai.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.francescoalessi.sagai.AppDestinations
import com.francescoalessi.sagai.R

sealed class BottomBarItem(
    val route: String,
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int
) {
    data object Home : BottomBarItem(
        AppDestinations.Home.name,
        R.string.app_name,
        R.drawable.ic_launcher_foreground
    )
    data object Character : BottomBarItem(
        AppDestinations.Character.name,
        R.string.app_name,
        R.drawable.ic_launcher_foreground
    )
    data object Settings : BottomBarItem(
        AppDestinations.Settings.name,
        R.string.app_name,
        R.drawable.ic_launcher_foreground
    )
}
