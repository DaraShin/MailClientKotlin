package com.shinkevich.mailclientkotlin.ui.maillist

enum class NavOptions {
    INCOMING,
    SENT,
    DRAFT,
    FAVOURITE,
    SPAM,
    DEFERRED,
    LOGOUT
}

sealed class NavigationItem(val route: String) {
    object Incoming : NavigationItem(NavOptions.INCOMING.name)
    object Sent : NavigationItem(NavOptions.SENT.name)
    object Draft : NavigationItem(NavOptions.DRAFT.name)
    object Favourite : NavigationItem(NavOptions.FAVOURITE.name)
    object Spam : NavigationItem(NavOptions.SPAM.name)
    object Deferred : NavigationItem(NavOptions.DEFERRED.name)
    object Logout : NavigationItem(NavOptions.LOGOUT.name)
}