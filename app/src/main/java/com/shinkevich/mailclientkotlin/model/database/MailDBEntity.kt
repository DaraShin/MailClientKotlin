package com.shinkevich.mailclientkotlin.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shinkevich.mailclientkotlin.model.MailType

@Entity(tableName = "mail")
data class MailDBEntity(
    @PrimaryKey
    @ColumnInfo(name = "mail_id")
    var mailID: String = "",

    @ColumnInfo(name = "message_uid")
    var messageUID: Long,

    @ColumnInfo(name = "author_email")
    var authorEmail: String,

    @ColumnInfo(name = "author_name")
    var authorName: String,

    @ColumnInfo(name = "recipient_email")
    var recipientEmail: String,
    var topic: String,
    var content: String,
    var date: String,

    @ColumnInfo(name = "is_read")
    var isRead: Boolean = false,

    @ColumnInfo(name = "is_in_favourites")
    var isInFavourites: Boolean = false,

    @ColumnInfo(name = "mail_type")
    var mailType: MailType
)