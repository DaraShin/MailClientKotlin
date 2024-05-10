package com.shinkevich.mailclientkotlin.ui.writemail

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
import com.shinkevich.mailclientkotlin.ui.theme.MailClientKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteMailActivity : ComponentActivity() {
    private var viewModel: WriteMailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = intent.extras
        var draftId: String? = null
        if (args != null && args.containsKey(MAIL_TO_EDIT_ID_EXTRA)) {
            draftId = args.getString(MAIL_TO_EDIT_ID_EXTRA)
        }
        setContent {
            MailClientKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    viewModel = hiltViewModel<WriteMailViewModel>()
                    WriteMailScreen(draftId, viewModel!!) {
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val MAIL_TO_EDIT_ID_EXTRA = "mail_to_edit_id_extra"
        fun launchWriteMailActivity(context: Context) {
            val intent = Intent(context, WriteMailActivity::class.java)
            context.startActivity(intent)
        }

        fun launchWriteMailActivity(context: Context, draftId: String) {
            val intent = Intent(context, WriteMailActivity::class.java)
            intent.putExtra(MAIL_TO_EDIT_ID_EXTRA, draftId)
            context.startActivity(intent)
        }
    }
}