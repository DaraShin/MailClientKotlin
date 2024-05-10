package com.shinkevich.mailclientkotlin.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shinkevich.mailclientkotlin.ui.theme.White

@Composable
fun LoadingDialog(message: String) {
    Dialog(onDismissRequest = { }) {
        Column(
            Modifier
                .background(White)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .clip(
                    RoundedCornerShape(8.dp)
                )
        ) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                message,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}