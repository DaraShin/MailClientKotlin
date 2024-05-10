package com.shinkevich.mailclientkotlin.model.mappers

import com.shinkevich.mailclientkotlin.model.database.MailDBEntity
import com.shinkevich.mailclientkotlin.model.network.EmailUser
import com.shinkevich.mailclientkotlin.model.network.MailSrv
import com.shinkevich.mailclientkotlin.ui.model.Mail
import java.text.SimpleDateFormat
import java.util.*

val dbDateFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US)

fun mailDBToMailUI(mail: MailDBEntity): Mail {

    return Mail(
        mail.mailID,
        mail.messageUID,
        mail.authorEmail,
        mail.authorName,
        listOf(mail.recipientEmail),
        mail.topic,
        mail.content,
        dbDateFormat.parse(
            mail.date
        ),
        mail.isRead,
        mail.isInFavourites,
        mail.mailType
    )
}

fun mailUIToMailDB(mail: Mail): MailDBEntity {
    return MailDBEntity(
        mail.mailID,
        mail.messageUID,
        mail.authorEmail,
        mail.authorName,
        if(mail.recipients.isNotEmpty()) mail.recipients[0] else "",
        mail.topic,
        mail.text,
        dbDateFormat.format(mail.date),
        mail.isRead,
        mail.isInFavourites,
        mail.mailType
    )
}

fun mailSrvToMailUI(mail: MailSrv): Mail {
    return Mail(
        mail.mailID,
        mail.messageUID,
        mail.author.email,
        mail.author.name,
        mail.recipients.map { recipient -> recipient.email },
        mail.topic,
        mail.content,
        mail.date,
        mail.isRead,
        mail.isInFavourites,
        mail.mailType
    )
}

fun mailUIToMailServer(mail: Mail): com.shinkevich.mailclientkotlin.model.network.MailSrv {
    val recipientsList = mail.recipients.map { recipient ->
        EmailUser(recipient, "")
    }
    return MailSrv(
        mail.mailID,
        mail.messageUID,
        EmailUser(mail.authorEmail, mail.authorName),
        recipientsList,
        mail.topic,
        mail.text,
        mail.date,
        mail.isRead,
        mail.isInFavourites,
        mail.mailType
    )
}
