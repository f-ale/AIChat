package com.francescoalessi.parla.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.francescoalessi.parla.R

sealed class BottomBarItem(
    val route: String,
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int
) {
    data object Home : BottomBarItem(
        AppDestinations.Home.name,
        R.string.nav_chat,
        R.drawable.outline_chat_24
    )
    data object Character : BottomBarItem(
        AppDestinations.Character.name,
        R.string.nav_characters,
        R.drawable.baseline_person_24
    )
    data object Settings : BottomBarItem(
        AppDestinations.Settings.name,
        R.string.nav_settings,
        R.drawable.baseline_settings_24
    )
}
