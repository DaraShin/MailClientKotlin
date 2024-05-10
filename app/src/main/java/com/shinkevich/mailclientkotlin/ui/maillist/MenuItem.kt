package com.shinkevich.mailclientkotlin.ui.maillist

import androidx.compose.ui.graphics.painter.Painter

data class MenuItem(
    val id: String,
    val title: String,
    val icon: Painter,
    val onClick : () -> Unit
)