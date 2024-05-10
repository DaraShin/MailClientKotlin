package com.shinkevich.mailclientkotlin.ui.maillist

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.ui.login.launchLoginActivity

@Composable
fun MailListNavHost(navController: NavHostController, viewModel: MailListViewModel) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Incoming.route
    ) {
        composable(route = NavigationItem.Incoming.route) { /*backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("mail_list")
            }
            val parentViewModel = hiltViewModel<MailListViewModel>(parentEntry)*/
            //if(it.destination.route != navController.currentDestination?.route) {
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.INCOMING, viewModel)
            //}
        }
        composable(route = NavigationItem.Sent.route) {
            //if(it.destination.route != navController.currentDestination?.route){
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.SENT, viewModel)
            //}
        }
        composable(route = NavigationItem.Deferred.route) {
            //if(it.destination.route != navController.currentDestination?.route) {
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.DEFERRED, viewModel)
            //}
        }
        composable(route = NavigationItem.Draft.route) {
            //if(it.destination.route != navController.currentDestination?.route) {
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.DRAFT, viewModel)
            //}
        }
        composable(route = NavigationItem.Favourite.route) {
            //if(it.destination.route != navController.currentDestination?.route) {
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.FAVOURITE, viewModel)
            //}
        }
        composable(route = NavigationItem.Spam.route) {
            //if(it.destination.route != navController.currentDestination?.route) {
            viewModel.setAppBarTitle(
                navController.currentBackStackEntry?.destination?.route ?: ""
            )
            MailListScreenContent(mailType = MailType.SPAM, viewModel)
            //}
        }
        composable(route = NavigationItem.Logout.route) {
            viewModel.logout()
            launchLoginActivity(LocalContext.current)
        }
    }
}