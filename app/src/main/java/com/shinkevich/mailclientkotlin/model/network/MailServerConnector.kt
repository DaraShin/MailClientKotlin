package com.shinkevich.mailclientkotlin.model.network

import android.util.Log
import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.model.User
import com.shinkevich.mailclientkotlin.model.network.exceptions.OperationException
import com.sun.mail.imap.IMAPStore
import javax.inject.Inject
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailServerConnector @Inject constructor(private val serverProperties: MailServerProperties) {

    fun getMails(
        mailType: MailType,
        user: User
    ): List<MailSrv> {
        return when (mailType) {
            MailType.INCOMING, MailType.SENT, MailType.DRAFT, MailType.SPAM -> getMailsFromFolder(
                serverProperties.gmailFolders[mailType]!!,
                user
            ).map { mail ->
                mail.mailType = mailType
                return@map mail
            }
            MailType.FAVOURITE, MailType.DEFERRED -> listOf()
        }
    }

    private fun getMailsFromFolder(
        folder: String,
        user: User
    ): List<MailSrv> {
        val emailSession = Session.getDefaultInstance(serverProperties.gmailImapProperties)
        val mailList = mutableListOf<MailSrv>()

        val emailStore = emailSession.getStore(serverProperties.imapMailStoreType) as IMAPStore
        emailStore.connect(user.email, user.password)

        val emailFolder = emailStore.getFolder(folder)
        val uidFolder = emailFolder as UIDFolder
        emailFolder.open(Folder.READ_ONLY)

        val messages = emailFolder.messages

        for (message in messages) {
            val mimeMessage = message as MimeMessage
            val nextMail =
                messageToMailSrv(message, uidFolder.getUID(message), mimeMessage.messageID)
            nextMail.isInFavourites = false
            mailList.add(nextMail)
        }

        emailFolder.close(false)
        emailStore.close()

        return mailList
    }


    fun sendMail(mail: MailSrv, user: User): Boolean {
        val session =
            Session.getInstance(serverProperties.gmailSmtpProperties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(user.email, user.password)
                }
            })

        try {
            val message = mailSrvToMimeMessage(mail, session, user)
            Transport.send(message)
        } catch (e: MessagingException) {
            return false
        }
        return true
    }

    fun deleteMail(mail: MailSrv, user: User) {
        setMessageFlag(mail, user, Flags(Flags.Flag.DELETED), true)
    }

    fun sendMailIsSeen(mail: MailSrv, user: User) {
        setMessageFlag(mail, user, Flags(Flags.Flag.SEEN), mail.isRead)
    }

    private fun setMessageFlag(mail: MailSrv, user: User, flags: Flags, isSet: Boolean) {
        val emailSession = Session.getDefaultInstance(serverProperties.gmailImapProperties)
        try {
            val emailStore = emailSession.getStore(serverProperties.imapMailStoreType) as IMAPStore
            emailStore.connect(user.email, user.password)
            val folder: String = serverProperties.gmailFolders[mail.mailType] ?: return
            val emailFolder = emailStore.getFolder(folder)
            emailFolder.open(Folder.READ_WRITE)
            val uidFolder = emailFolder as UIDFolder
            val message = uidFolder.getMessageByUID(mail.messageUID) ?: return
            message.setFlags(flags, isSet)
            emailFolder.close(false)
            emailStore.close()
        } catch (e: NoSuchProviderException) {
            Log.i("", Log.getStackTraceString(e))
            throw OperationException()
        } catch (e: MessagingException) {
            Log.i("", Log.getStackTraceString(e))
            throw OperationException()
        }
    }

    fun saveDraft(mail: MailSrv, user: User): MailSrv? {
        val emailSession = Session.getDefaultInstance(serverProperties.gmailImapProperties)
        return try {
            val emailStore = emailSession.getStore(serverProperties.imapMailStoreType) as IMAPStore
            emailStore.connect(user.email, user.password)
            val emailFolder = emailStore.getFolder(serverProperties.gmailFolders[MailType.DRAFT])
            emailFolder.open(Folder.READ_WRITE)

            val message = mailSrvToMimeMessage(mail, emailSession, user)
            if(mail.recipients.isNotEmpty() && mail.recipients[0].email.isNotEmpty()){
                message.setRecipients(
                    Message.RecipientType.TO,
                    arrayOf<Address>(InternetAddress(mail.recipients[0].email))
                )
            }

            message.setFlags(Flags(Flags.Flag.DRAFT), true)
            emailFolder.appendMessages(arrayOf(message))

            val addedDraftMessage = emailFolder.getMessage(emailFolder.messageCount)

            val uidFolder = emailFolder as UIDFolder
            val draftUID = uidFolder.getUID(addedDraftMessage)

            val addedDraftMimeMessage =
                emailFolder.getMessage(emailFolder.messageCount) as MimeMessage
            val draftID = addedDraftMimeMessage.messageID

            val draftMail = messageToMailSrv(addedDraftMessage, draftUID, draftID)

            emailFolder.close(false)
            emailStore.close()

            draftMail
        } catch (e: NoSuchProviderException) {
            Log.i("", Log.getStackTraceString(e))
            null
        } catch (e: MessagingException) {
            Log.i("", Log.getStackTraceString(e))
            null
        }
    }

    fun updateDraft(mail: MailSrv, user: User): Boolean {
        val emailSession = Session.getDefaultInstance(serverProperties.gmailImapProperties)
        var emailFolder: Folder? = null
        var emailStore: IMAPStore? = null
        return try {
            emailStore = emailSession.getStore(serverProperties.imapMailStoreType) as IMAPStore
            emailStore.connect(user.email, user.password)
            emailFolder = emailStore.getFolder("[Gmail]/Черновики")
            emailFolder.open(Folder.READ_WRITE)
            val uidFolder = emailFolder as UIDFolder

            val draftMessage: Message = uidFolder.getMessageByUID(mail.messageUID) ?: return false

            setMessageContent(draftMessage, mail, user)
            draftMessage.setRecipients(
                Message.RecipientType.TO,
                arrayOf<Address>(InternetAddress(user.email))
            )
            draftMessage.setFlags(Flags(Flags.Flag.DRAFT), true)



            true
        } catch (e: NoSuchProviderException) {
            Log.i("", Log.getStackTraceString(e))
            false
        } catch (e: MessagingException) {
            Log.i("", Log.getStackTraceString(e))
            false
        } finally {
            emailFolder?.close(false)
            emailStore?.close()
        }
    }

}