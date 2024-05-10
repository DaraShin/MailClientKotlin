package com.shinkevich.mailclientkotlin.ui.maillist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinkevich.mailclientkotlin.ui.theme.*
import com.shinkevich.mailclientkotlin.R

@Composable
fun DrawerHeader(email : String) {
    Box(modifier = Modifier.background(GreenPrimary).fillMaxWidth()){
        Text(text = email,
        color = White,
        fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.Center).padding(top = 16.dp))
    }
}

@Preview
@Composable
fun DrawerHeaderPreview(){
    DrawerHeader(email = "mail.example@gmail.com")
}

@Composable
fun DrawerBody(
    menuItems: List<MenuItem>,
    selectedDrawerItem: String,
) {
    LazyColumn(Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)) {
        items(menuItems) { menuItem ->
            Row(modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (menuItem.id == selectedDrawerItem) GreenLight else White)
                .clickable {
                    menuItem.onClick()
                }
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, bottom = 8.dp)) {
                Icon(
                    painter = menuItem.icon,
                    contentDescription = menuItem.title, Modifier.size(
                        dimensionResource(id = R.dimen.icon_size)
                    ),
                    tint = if(menuItem.id == selectedDrawerItem) GreenText else Black,
                )
                Text(
                    text = menuItem.title,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically),
                    color = if(menuItem.id == selectedDrawerItem) GreenText else Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}