package com.androsmith.wiflow.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {

    @Serializable
    object Home: Screens

    @Serializable
    object Settings: Screens

    @Serializable
    object Welcome: Screens
}
