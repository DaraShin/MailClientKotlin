package com.shinkevich.mailclientkotlin.ui.maillist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shinkevich.mailclientkotlin.R
import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.ui.maildetails.MailDetailsActivity
import com.shinkevich.mailclientkotlin.ui.model.Mail
import com.shinkevich.mailclientkotlin.ui.showDate
import com.shinkevich.mailclientkotlin.ui.theme.Gray
import com.shinkevich.mailclientkotlin.ui.theme.GreenPrimary
import com.shinkevich.mailclientkotlin.ui.theme.White
import com.shinkevich.mailclientkotlin.ui.writemail.WriteMailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MailListScreen() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val mailListNavController = rememberNavController()
    val viewModel = hiltViewModel<MailListViewModel>()
    val topAppBarTitle = remember {
        viewModel.topBarTitle
    }
    val context = LocalContext.current
    var selectedMenuItem by rememberSaveable { mutableStateOf(NavigationItem.Incoming.route) }

    val menuItems = listOf(
        MenuItem(
            id = NavigationItem.Incoming.route,
            stringResource(id = R.string.incoming),
            icon = painterResource(
                id = R.drawable.icon_incoming
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Incoming.route
                navigateToScreenFromDrawer(
                    NavigationItem.Incoming.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ),
        MenuItem(
            id = NavigationItem.Sent.route,
            stringResource(id = R.string.sent),
            icon = painterResource(
                id = R.drawable.icon_send
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Sent.route
                navigateToScreenFromDrawer(
                    NavigationItem.Sent.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ),
        MenuItem(
            id = NavigationItem.Deferred.route,
            stringResource(id = R.string.deferred),
            icon = painterResource(
                id = R.drawable.icon_deffered_send
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Deferred.route
                navigateToScreenFromDrawer(
                    NavigationItem.Deferred.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ),
        MenuItem(
            id = NavigationItem.Draft.route,
            stringResource(id = R.string.drafts),
            icon = painterResource(
                id = R.drawable.icon_draft
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Draft.route
                navigateToScreenFromDrawer(
                    NavigationItem.Draft.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ), MenuItem(
            id = NavigationItem.Favourite.route,
            stringResource(id = R.string.favourites),
            icon = painterResource(
                id = R.drawable.icon_star
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Favourite.route
                navigateToScreenFromDrawer(
                    NavigationItem.Favourite.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ), MenuItem(
            id = NavigationItem.Spam.route,
            stringResource(id = R.string.spam),
            icon = painterResource(
                id = R.drawable.icon_spam
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Spam.route
                navigateToScreenFromDrawer(
                    NavigationItem.Spam.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        ),
        MenuItem(
            id = NavigationItem.Logout.route,
            stringResource(id = R.string.quit),
            icon = painterResource(
                id = R.drawable.icon_quit
            ),
            onClick = {
                selectedMenuItem = NavigationItem.Logout.route
                navigateToScreenFromDrawer(
                    NavigationItem.Logout.route,
                    mailListNavController,
                    scaffoldState.drawerState,
                    coroutineScope
                )
            }
        )
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarMailList(
                title = topAppBarTitle,
                onNavigationIconClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
        },
        drawerContent = {
            DrawerHeader(email = viewModel.getUserEmail())
            DrawerBody(
                menuItems = menuItems,
                selectedDrawerItem = selectedMenuItem
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { WriteMailActivity.launchWriteMailActivity(context)},
                backgroundColor = GreenPrimary,
                contentColor = White,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_plus),
                    contentDescription = "write mail",
                    Modifier.padding(16.dp)
                )
            }
        },
    ) {
        MailListNavHost(mailListNavController, viewModel)
    }
}

fun navigateToScreenFromDrawer(
    route: String,
    navController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        drawerState.close()
    }
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId)
        launchSingleTop = true
    }

}

@Composable
fun MailListScreenContent(
    mailType: MailType,
    viewModel: MailListViewModel
) {
    Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
        //Text(text = mailType.name)
        MailList(
            viewModel.getMailList(mailType),
            viewModel,
            mailType == MailType.FAVOURITE
        )
    }

}

@Composable
fun MailList(
    mailListState: MutableState<List<Mail>>,
    viewModel: MailListViewModel,
    showCategory: Boolean = false
) {

    /*val mailListStateRemembered = remember {
        mutableStateOf(testMailList)
    }*/
    val mailListStateRemembered = remember { mailListState }
    LazyColumn {
        items(mailListStateRemembered.value) { mail ->
            MailItem(mail, viewModel, showCategory)
            Divider()
        }
    }
}

@Composable
fun MailItem(
    mail: Mail,
    viewModel: MailListViewModel,
    showCategory: Boolean = false
) {
    var isInFavourites by remember { mutableStateOf(mail.isInFavourites) }
    val context = LocalContext.current
    Row(modifier = Modifier
        .padding(top = 8.dp, bottom = 10.dp)
        .clickable {
            viewModel.setMailIsSeen(mail)
            MailDetailsActivity.launchMailDetailsActivity(context, mail.mailID)
        }) {
        Column(
            Modifier
                .align(Alignment.CenterVertically)
                .weight(0.9F)
        ) {
            Text(
                text = when (mail.mailType) {
                    MailType.INCOMING, MailType.SPAM -> mail.authorEmail
                    MailType.SENT, MailType.DEFERRED, MailType.DRAFT -> if (mail.recipients.isNotEmpty()) mail.recipients[0] else ""
                    else -> mail.authorEmail
                },
                fontWeight = if (!mail.isRead) FontWeight.Bold else FontWeight.Normal,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (mail.topic.isNotEmpty()) {
                Text(
                    text = mail.topic,
                    fontWeight = if (!mail.isRead) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (showCategory) {
                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.green1Light),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = dimensionResource(id = R.dimen.border_width),
                            color = colorResource(id = R.color.green6),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = mail.mailType.getName(LocalContext.current),
                        fontSize = 16.sp
                    )
                }
            }
            Text(
                text = mail.text,
                maxLines = if (mail.topic.isNotEmpty()) 2 else 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                color = Gray
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Column(
            Modifier
                .align(Alignment.Top)
        ) {
            Text(
                text = showDate(mail.date),
                fontSize = 16.sp,
                color = Gray
            )
            IconButton(
                onClick = {
                    isInFavourites = !isInFavourites
                    mail.isInFavourites = isInFavourites
                    viewModel.updateInFavourites(mail)
                },
                Modifier
                    .size(dimensionResource(id = R.dimen.icon_size))
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            ) {
                Icon(
                    painter = if (isInFavourites) painterResource(id = R.drawable.icon_star_selected)
                    else painterResource(id = R.drawable.icon_star),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun AppBarMailList(title: MutableState<String>, onNavigationIconClick: () -> Unit) {
    var titleRemembered = remember { title }
    TopAppBar(
        title = { Text(titleRemembered.value, fontSize = 26.sp) },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_menu),
                    //imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.top_bar_icon_size))
                )
            }
        },
        actions = {
            IconButton(
                onClick = {/*todo*/ },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_refresh),
                    //imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.top_bar_icon_size))
                )
            }
        })
}
