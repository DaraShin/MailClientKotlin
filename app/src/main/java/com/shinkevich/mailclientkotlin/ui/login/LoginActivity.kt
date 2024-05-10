package com.shinkevich.mailclientkotlin.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinkevich.mailclientkotlin.ui.maillist.launchMailListActivity
import com.shinkevich.mailclientkotlin.ui.theme.MailClientKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MailClientKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val loginViewModel = hiltViewModel<LoginViewModel>()
                    if(!loginViewModel.navigateOnAuthenticated.value){
                        LoginScreen(viewModel = loginViewModel)
                    } else {
                        launchMailListActivity(context = this)
                    }
                }
            }
        }
    }
}