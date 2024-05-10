package com.shinkevich.mailclientkotlin.model.network.auth

import com.shinkevich.mailclientkotlin.model.network.MailServerProperties
import com.sun.mail.imap.IMAPStore
import javax.inject.Inject
import javax.mail.AuthenticationFailedException
import javax.mail.Session

class ServerAuth @Inject constructor(val serverProperties: MailServerProperties){

    fun checkAuthenticationData(login: String, password: String): Boolean {
        val emailSession = Session.getDefaultInstance(serverProperties.gmailImapProperties)
        try {
            val emailStore = emailSession.getStore(serverProperties.imapMailStoreType) as IMAPStore
            emailStore.connect(login, password)
            emailStore.close()
            return true
        } catch (e: AuthenticationFailedException) {
            return false
        }
    }
}