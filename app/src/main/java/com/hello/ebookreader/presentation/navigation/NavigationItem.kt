package com.hello.ebookreader.presentation.navigation

import kotlinx.serialization.Serializable


sealed class NavigationItem {

    @Serializable
    object HomeScreen

    @Serializable
    data class BooksByCategory(val category: String)

    @Serializable
    data class ShowPdfScreen(val url: String, val bookName: String)

    @Serializable
    object AddBookScreen
}