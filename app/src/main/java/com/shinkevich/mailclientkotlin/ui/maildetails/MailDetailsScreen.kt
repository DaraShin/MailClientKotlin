package com.shinkevich.mailclientkotlin.ui.maildetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shinkevich.mailclientkotlin.ui.showDate
import com.shinkevich.mailclientkotlin.R
import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.ui.composables.LoadingDialog
import com.shinkevich.mailclientkotlin.ui.model.Mail
import com.shinkevich.mailclientkotlin.ui.theme.Gray
import com.shinkevich.mailclientkotlin.ui.theme.White
import com.shinkevich.mailclientkotlin.ui.writemail.WriteMailActivity

@Composable
fun MailDetailsScreen(
    navController: NavHostController,
    viewModel: MailDetailsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val mail by remember { viewModel.mail }

    var isDeleting by remember { viewModel.isDeleting }
    var isDeleted by remember { viewModel.isDeleted }
    var navigateBack by remember { viewModel.navigateBack }

    if (isDeleting) {
        LoadingDialog(message = stringResource(id = R.string.deleting_mail))
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            Toast.makeText(
                context,
                if (isDeleted) context.getString(R.string.mail_deleted)
                else context.getString(R.string.mail_deleting_error),
                Toast.LENGTH_SHORT
            )
                .show()
            onBack()
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {

                IconButton(
                    onClick = { onBack() },
                    Modifier
                        .size(dimensionResource(id = R.dimen.top_bar_icon_size))
                        .offset(x = dimensionResource(id = R.dimen.content_horizontal_margin))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = "back",
                        Modifier.fillMaxSize(),
                        tint = White
                    )
                }
            },
            actions = {
                if (mail != null && mail!!.mailType == MailType.DRAFT) {
                    IconButton(
                        onClick = {
                                  WriteMailActivity.launchWriteMailActivity(context, mail!!.mailID)
                        },
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_edit),
                            contentDescription = "delete",
                            tint = White,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.top_bar_icon_size))
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.deleteMail() },
                    Modifier
                        .padding(end = 6.dp)
                    //.size(dimensionResource(id = R.dimen.top_bar_icon_size))
                    //.offset(x = (-10).dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_delete),
                        contentDescription = "delete",
                        tint = White,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.top_bar_icon_size))
                    )
                }
            }
        )
    }) {
        if (mail != null) {
            MailDetailsScreenContent(viewModel.mail.value!!, viewModel)
        }

    }
}

@Composable
fun MailDetailsScreenContent(mail: Mail, viewModel: MailDetailsViewModel) {
    var isInFavourites by remember { mutableStateOf(mail.isInFavourites) }

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = dimensionResource(id = R.dimen.base_text_size).value.sp
        )
    ) {
        Column(
            Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen.content_horizontal_margin
                ), vertical = 10.dp
            )
        ) {
            Box(Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = if (mail.topic.isNotEmpty()) mail.topic else stringResource(id = R.string.without_topic),
                        fontSize = dimensionResource(id = R.dimen.title_text_size).value.sp
                    )
                    Text(
                        text = showDate(mail.date),
                        color = Gray,
                        fontSize = 18.sp
                    )
                }
                IconButton(
                    onClick = {
                        isInFavourites = !isInFavourites
                        mail.isInFavourites = isInFavourites
                        viewModel.updateInFavourites(mail)
                    },
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.icon_size))
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = if (isInFavourites) painterResource(id = R.drawable.icon_star_selected)
                        else painterResource(id = R.drawable.icon_star),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            Row {
                Column {
                    Text(text = stringResource(id = R.string.from) + ":", fontSize = 20.sp)
                    Text(text = stringResource(id = R.string.to) + ":", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(text = mail.authorEmail, fontSize = 20.sp)
                    Text(
                        text = if (mail.recipients.isNotEmpty()) mail.recipients[0] else "",
                        fontSize = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = mail.text,
                    textAlign = TextAlign.Justify,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

