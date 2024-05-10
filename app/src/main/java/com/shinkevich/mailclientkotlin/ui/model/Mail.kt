package com.shinkevich.mailclientkotlin.ui.model

import com.shinkevich.mailclientkotlin.model.MailType
import java.util.*

data class Mail(
    var mailID: String = "",
    var messageUID: Long,
    var authorEmail: String,
    var authorName: String,
    var recipients: List<String>,
    var topic: String,
    var text: String,
    var date: Date,
    var isRead: Boolean,
    var isInFavourites: Boolean,
    var mailType: MailType
) {
    constructor() : this(
        "",
        0,
        "",
        "",
        mutableListOf(),
        "",
        "",
        Date(),
        false,
        false,
        MailType.SENT
    )

    fun addRecipient(recipient : String){
        (recipients as MutableList<String>).add(recipient)
    }
}