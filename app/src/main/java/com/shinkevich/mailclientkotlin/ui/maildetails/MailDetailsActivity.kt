package com.shinkevich.mailclientkotlin.ui.maildetails

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
import androidx.navigation.compose.rememberNavController
import com.shinkevich.mailclientkotlin.ui.theme.MailClientKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MailDetailsActivity : ComponentActivity() {
    private var viewModel: MailDetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = intent.extras
        var mailId: String? = null
        if (args != null && args.containsKey(MAIL_TO_SHOW_ID_EXTRA)) {
            mailId = args.getString(MAIL_TO_SHOW_ID_EXTRA)
        }
        setContent {
            MailClientKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    viewModel = hiltViewModel<MailDetailsViewModel>()
                    if(mailId != null) {
                        viewModel!!.getMail(mailId)
                    }
                    val navController = rememberNavController()
                    MailDetailsScreen(navController, viewModel!!){
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val MAIL_TO_SHOW_ID_EXTRA = "mail_to_show_id_extra"

        fun launchMailDetailsActivity(context: Context, mailId: String) {
            val intent = Intent(context, MailDetailsActivity::class.java)
            intent.putExtra(MAIL_TO_SHOW_ID_EXTRA, mailId)
            context.startActivity(intent)
        }
    }
}