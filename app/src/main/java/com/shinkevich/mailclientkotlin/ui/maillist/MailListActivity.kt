package com.shinkevich.mailclientkotlin.ui.maillist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.shinkevich.mailclientkotlin.ui.theme.MailClientKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchMailListActivity(context: Context) {
    val intent = Intent(context, MailListActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

@AndroidEntryPoint
class MailListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MailClientKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MailListScreen()
                }
            }
        }
    }

//    override fun onBackPressed() {
//        finish()
//        if (navView.getCheckedItem().getItemId() === R.id.incoming_item) {
//            //super.onBackPressed();
//            finish()
//        } else {
//            navView.setCheckedItem(R.id.incoming_item)
//            navigationItemSelectedListener.onNavigationItemSelected(navView.getCheckedItem())
//        }
//    }
}