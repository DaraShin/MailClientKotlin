package com.shinkevich.mailclientkotlin.model.network

import com.shinkevich.mailclientkotlin.model.User
import javax.mail.Address
import javax.mail.Flags
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

fun mailSrvToMimeMessage(mail: MailSrv, session: Session, user: User): MimeMessage {
    val message = MimeMessage(session)
    setMessageContent(message, mail,user)
    return message
}

fun setMessageContent(message: Message, mail: MailSrv, user : User){
    message.setFrom(InternetAddress(user.email))
    message.subject = mail.topic
    message.setText(mail.content)
    message.setRecipients(Message.RecipientType.TO, arrayOf<InternetAddress>())
    for (recipient in mail.recipients) {
        if(recipient.email.isNotEmpty()) {
            message.addRecipient(Message.RecipientType.TO, InternetAddress(recipient.email))
        }
    }
}

fun mailSrvToDraftMimeMessage(mail: MailSrv, session: Session, user: User): MimeMessage{
    val message = MimeMessage(session)
    message.subject = mail.topic
    message.setText(mail.content)
    message.setFrom(InternetAddress(user.email))
    message.setRecipients(
        Message.RecipientType.TO,
        arrayOf<Address>(InternetAddress(user.email))
    )
    message.setFlags(Flags(Flags.Flag.DRAFT), true)
    return message
}

fun messageToMailSrv(message: Message, uid: Long, mailId: String): MailSrv {
    val mail = MailSrv()
    mail.mailID = mailId
    mail.messageUID = uid

    if (message.from.isNotEmpty()) {
        mail.author = EmailUser(
            (message.from[0] as InternetAddress).address ?: "",
            (message.from[0] as InternetAddress).personal ?: ""
        )
    }

    if (message.allRecipients != null && message.allRecipients.isNotEmpty()) {
        for (recipient in message.allRecipients) {
            val recipientInternetAddress = recipient as InternetAddress
            mail.addRecipient(
                EmailUser(
                    recipientInternetAddress.address ?: "",
                    recipientInternetAddress.personal ?: ""
                )
            )
        }
    }

    mail.topic = message.subject ?: ""
    mail.content = getMessageString(message)
    mail.date = message.receivedDate
    mail.isRead = message.isSet(Flags.Flag.SEEN)
    mail.isInFavourites = message.isSet(Flags.Flag.FLAGGED)

    return mail
}

fun getMessageString(message: Message): String {
    try {
        if (message.isMimeType("text/plain")) {
            return message.content.toString()
        }
        if (message.isMimeType("multipart/*")) {
            val mimeMultipart = message.content as MimeMultipart
            var text = ""
            for (i in 0 until mimeMultipart.count) {
                val bodyPart = mimeMultipart.getBodyPart(i)
                if (bodyPart.isMimeType("text/plain")) {
                    text += bodyPart.content
                }
            }
            return text
        }
    } catch (exc: java.lang.Exception) {
    }
    return ""
}
