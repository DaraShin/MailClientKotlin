package com.shinkevich.mailclientkotlin.model.network

import com.shinkevich.mailclientkotlin.model.MailType
import java.util.*

class MailServerProperties {
    val gmailImapHost = "imap.gmail.com"
    val imapPort = "993"
    val imapMailStoreType = "imap"
    val gmailSmtpHost = "smtp.gmail.com"
    val smtpPort = "587"

    val gmailImapProperties = Properties()
    val gmailSmtpProperties = Properties()
    val gmailFolders = EnumMap<MailType, String>(MailType::class.java)

    init {
        gmailImapProperties["mail.imap.host"] = gmailImapHost
        gmailImapProperties["mail.imap.port"] = imapPort
        gmailImapProperties["mail.imap.auth"] = "true"
        gmailImapProperties["mail.imap.ssl.enable"] = "true"
        gmailImapProperties["mail.imap.ssl.protocols"] = "TLSv1.2"

        gmailSmtpProperties["mail.smtp.host"] = gmailSmtpHost
        gmailSmtpProperties["mail.smtp.port"] = smtpPort
        gmailSmtpProperties["mail.smtp.auth"] = "true"
        gmailSmtpProperties["mail.smtp.starttls.enable"] = "true"
        gmailSmtpProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2")

        gmailFolders[MailType.INCOMING] = "INBOX"
        gmailFolders[MailType.SENT] = "[Gmail]/Отправленные"
        gmailFolders[MailType.DRAFT] = "[Gmail]/Черновики"
        gmailFolders[MailType.SPAM] = "[Gmail]/Спам"
    }

}