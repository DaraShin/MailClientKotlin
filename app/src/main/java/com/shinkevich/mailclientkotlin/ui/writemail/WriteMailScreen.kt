package com.shinkevich.mailclientkotlin.ui.writemail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinkevich.mailclientkotlin.R
import com.shinkevich.mailclientkotlin.ui.composables.LoadingDialog
import com.shinkevich.mailclientkotlin.ui.theme.GreenLight
import com.shinkevich.mailclientkotlin.ui.theme.GreenPrimary
import com.shinkevich.mailclientkotlin.ui.theme.White

@Composable
fun WriteMailScreen(draftId: String?, viewModel: WriteMailViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    if (draftId != null && viewModel.needToInitDrafts) {
        viewModel.initAsDraft(draftId)
    }

    var showNoRecipientsAlert by remember { mutableStateOf(false) }
    var showSaveDraftDialog by remember { mutableStateOf(false) }
    var isSending by remember { viewModel.isSending }
    val isSavingDraft by remember { viewModel.isSavingDraft }
    val isDraftSaved by remember { viewModel.isDraftSavedStatus }
    val isSent by remember { viewModel.isSentStatus }
    val navigateBack by remember { viewModel.navigateBack }
    val showErrorToast by remember { viewModel.showErrorToast }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            if (isDraftSaved) {
                Toast.makeText(context, context.getString(R.string.draft_saved), Toast.LENGTH_SHORT)
                    .show()
            } else if (isSent) {
                Toast.makeText(context, context.getString(R.string.mail_sent), Toast.LENGTH_SHORT)
                    .show()
            }
            onBack()
        }
    }

    if (showErrorToast) {
        if (!isDraftSaved) {
            Toast.makeText(
                context,
                context.getString(R.string.draft_saving_error),
                Toast.LENGTH_SHORT
            )
                .show()
            onBack()
        } else if (!isSent) {
            Toast.makeText(
                context,
                context.getString(R.string.mail_sending_error),
                Toast.LENGTH_SHORT
            )
                .show()
            onBack()
        }
    }



    if (showNoRecipientsAlert) {
        AlertDialog(
            onDismissRequest = {
                showNoRecipientsAlert = false
            },
            text = { Text(stringResource(id = R.string.add_recipient), fontSize = 20.sp) },
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { showNoRecipientsAlert = false }
                    ) {
                        Text(stringResource(id = R.string.ok), fontSize = 20.sp)
                    }
                }

            }
        )
    }

    if (isSending) {
        LoadingDialog(stringResource(id = R.string.sending_mail))
    }

    if (isSavingDraft) {
        LoadingDialog(stringResource(id = R.string.saving_draft))
    }

    if (showSaveDraftDialog) {
        AlertDialog(
            onDismissRequest = {
                showNoRecipientsAlert = false
            },
            text = { Text(stringResource(id = R.string.save_draft), fontSize = 20.sp) },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround

                ) {
                    Button(
                        onClick = {
                            showSaveDraftDialog = false
                            onBack()
                        }
                    ) {
                        Text(stringResource(id = R.string.no), fontSize = 20.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.saveDraft()
                            showSaveDraftDialog = false

                        }
                    ) {
                        Text(stringResource(id = R.string.yes), fontSize = 20.sp)
                    }
                }

            }
        )
    }

    BackHandler {
        showSaveDraftDialog = true
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        showSaveDraftDialog = true
                    },
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
                IconButton(
                    onClick = {
                        if (viewModel.recipientEmail.value.isBlank()) {
                            showNoRecipientsAlert = true
                        } else {
                            viewModel.sendMail()
                        }

                    },
                    Modifier
                        .size(dimensionResource(id = R.dimen.top_bar_icon_size))
                        .offset(x = (-10).dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_send),
                        contentDescription = "send",
                        tint = White
                    )
                }
            }
        )
    }) {
        WriteMailScreenContent()
    }
}




@Composable
fun WriteMailScreenContent(viewModel: WriteMailViewModel = hiltViewModel()) {
    var authorEmail by remember { viewModel.recipientEmail }
    var subject by remember { viewModel.subject }
    var text by remember { viewModel.text }
    Column(
        Modifier
            .imePadding()
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(id = R.string.from) + ":",
                fontSize = dimensionResource(id = R.dimen.write_mail_text_column_text_size).value.sp,
                modifier = Modifier.width(dimensionResource(id = R.dimen.write_mail_text_column_width))
            )
            TextInputField(viewModel.getCurrentUserEmail(), {}, true)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(id = R.string.to) + ":",
                fontSize = dimensionResource(id = R.dimen.write_mail_text_column_text_size).value.sp,
                modifier = Modifier.width(dimensionResource(id = R.dimen.write_mail_text_column_width))
            )
            TextInputField(authorEmail, { newValue -> viewModel.recipientEmail.value = newValue }, false)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(id = R.string.subject) + ":",
                fontSize = dimensionResource(id = R.dimen.write_mail_text_column_text_size).value.sp,
                modifier = Modifier.width(dimensionResource(id = R.dimen.write_mail_text_column_width))
            )
            TextInputField(subject, { newValue -> viewModel.subject.value = newValue }, false)
        }
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { nextValue: String -> viewModel.text.value = nextValue },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            //.verticalScroll(rememberScrollState()),
            //.border(1.dp, GreenPrimary, RoundedCornerShape(10.dp)),
            //shape = RoundedCornerShape(10.dp),
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = GreenPrimary,
                unfocusedIndicatorColor = GreenPrimary,
                backgroundColor = GreenLight,

                ),
            placeholder = { Text(stringResource(id = R.string.enter_message_hint)) }
        )
    }
}

@Composable
fun TextInputField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false
) {
    BasicTextField(
        value = value,
        readOnly = readOnly,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontSize = 20.sp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    //.padding(horizontal = 10.dp) // margin left and right
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 4
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            color = GreenPrimary,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth.toFloat()
                        )
                    }
                    .padding(horizontal = 4.dp, vertical = 0.dp), // inner padding
            ) {
                /*if (value.isEmpty()) {
                    Text(
                        text = "hint",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray
                    )
                }*/
                innerTextField()
            }
        },
        cursorBrush = SolidColor(GreenPrimary)
    )
}
