package com.shinkevich.mailclientkotlin.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shinkevich.mailclientkotlin.R
import com.shinkevich.mailclientkotlin.ui.theme.*

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    if(viewModel.loading.value){
        Dialog(onDismissRequest = {  }) {
            CircularProgressIndicator()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = GreenBackground)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.app_name).uppercase(),
            color = GreenText,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
        )
        Column(
            Modifier
                .background(White)
                .padding(horizontal = 20.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (viewModel.showErrorMessage.value) {
                Text(text = viewModel.errorMessage.value, color = Red)
                Spacer(modifier = Modifier.height(10.dp))
            }
            TextField(
                value = viewModel.emailState.value,
                onValueChange = { newEmail: String ->
                    viewModel.emailState.value = newEmail
                    viewModel.validateEmail(newEmail)
                },
                placeholder = { Text(stringResource(id = R.string.email)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = GreenInputBackground,
                    placeholderColor = Gray,
                    cursorColor = GreenPrimary
                ),
                isError = !viewModel.emailValid.value
            )
            if (!viewModel.emailValid.value) {
                Text(
                    text = viewModel.emailValidationError.value,
                    color = Red,
                    style = MaterialTheme.typography.caption,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = viewModel.passwordState.value,
                onValueChange = { newPassword ->
                    viewModel.passwordState.value = newPassword
                    viewModel.validatePassword(newPassword)
                },
                singleLine = true,
                placeholder = { Text(stringResource(id = R.string.password)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = GreenInputBackground,
                    placeholderColor = Gray,
                    cursorColor = GreenPrimary
                ),
                trailingIcon = {
                    val image = if (viewModel.passwordVisible.value)
                        painterResource(id = R.drawable.icon_invisible)
                    else painterResource(id = R.drawable.icon_visible)

                    val description = if (viewModel.passwordVisible.value) "Hide password" else "Show password"

                    IconButton(onClick = { viewModel.passwordVisible.value = !viewModel.passwordVisible.value }) {
                        Icon(painter = image, description, Modifier.size(30.dp))
                    }
                },
                visualTransformation = if (viewModel.passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                isError = !viewModel.passwordValid.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrect = false
                )
            )
            if (!viewModel.passwordValid.value) {
                Text(
                    text = viewModel.passwordValidationError.value,
                    color = Red,
                    style = MaterialTheme.typography.caption,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.validateEmail(viewModel.emailState.value)
                    viewModel.validatePassword(viewModel.passwordState.value)
                    if (viewModel.emailValid.value && viewModel.passwordValid.value) {
                        viewModel.tryAuthenticate(viewModel.emailState.value, viewModel.passwordState.value)
                    }
                },
                Modifier.widthIn(min = 150.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = GreenPrimary)
            ) {
                Text(
                    text = stringResource(id = R.string.enter).uppercase(),
                    color = White
                )
            }
        }
    }
}