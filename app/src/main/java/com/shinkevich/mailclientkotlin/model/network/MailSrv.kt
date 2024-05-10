package com.shinkevich.mailclientkotlin.model.network

import com.shinkevich.mailclientkotlin.model.MailType
import java.util.Date

data class MailSrv(
    var mailID : String = "",
    var messageUID: Long,
    var author : EmailUser,
    var recipients: List<EmailUser> = mutableListOf(),
    var topic: String,
    var content: String,
    var date: Date,
    var isRead: Boolean,
    var isInFavourites: Boolean,
    var mailType: MailType
) {
    constructor() : this(
        "",
        0,
        EmailUser("",""),
        mutableListOf(),
        "",
        "",
        Date(),
        false,
        false,
        MailType.INCOMING
    )

    fun addRecipient(recipient : EmailUser){
        (recipients as MutableList<EmailUser>).add(recipient)
    }
}

data class EmailUser(
    var email : String,
    var name : String
)