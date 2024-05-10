package com.shinkevich.mailclientkotlin.model.repository

import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.model.User
import com.shinkevich.mailclientkotlin.model.database.MailDAO
import com.shinkevich.mailclientkotlin.model.mappers.*
import com.shinkevich.mailclientkotlin.model.network.MailServerConnector
import com.shinkevich.mailclientkotlin.model.network.exceptions.OperationException
import com.shinkevich.mailclientkotlin.ui.model.Mail
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class MailRepository @Inject constructor(
    private val mailDAO: MailDAO,
    private val mailServerConnector: MailServerConnector
) {
    fun getMailsByType(mailType: MailType, user: User): Flow<List<Mail>> {
        //CoroutineScope(Dispatchers.IO).launch {  mailDAO.clear() }
        return when (mailType) {
            MailType.INCOMING, MailType.SENT, MailType.DRAFT, MailType.SPAM ->
                merge(
                    getMailsFromDB(mailType)
                        .dropWhile { mailList -> mailList.isEmpty() },
                    getMailsFromServer(mailType, user)
                )
            MailType.FAVOURITE, MailType.DEFERRED -> getMailsFromDB(mailType)
        }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    fun getMailsFromDB(mailType: MailType): Flow<List<Mail>> {
        return flow {
            when (mailType) {
                MailType.FAVOURITE -> emit(mailDAO.getFavouriteMails())
                else -> emit(mailDAO.getMailsByType(mailType))
            }
        }.map { mailDBList -> listMapper(mailDBList, ::mailDBToMailUI) }
    }

    fun getMailsFromServer(mailType: MailType, user: User): Flow<List<Mail>> {
        return flow {
            emit(
                mailServerConnector.getMails(
                    mailType,
                    user
                )
            )
        }.map { mailResponseList ->
            listMapper(
                mailResponseList,
                ::mailSrvToMailUI
            )
        }.onEach { mailList ->
            for (mail in mailList) {
                //if (mailDAO.getMailByPK(mail.mailID).isEmpty()) {
                if (mailDAO.getMailByPK(mail.mailID) == null) {
                    mail.mailType = mailType
                    mailDAO.insertMail(mailUIToMailDB(mail))
                }
            }
        }
    }

    fun sendMail(mail: Mail, user: User): Flow<Boolean> {
        return flow {
            val isSent = mailServerConnector.sendMail(mailUIToMailServer(mail), user)
            emit(isSent)
        }.flowOn(Dispatchers.IO)
    }

    fun setMailIsSeen(mail: Mail, user: User) {
        if (mail.mailType == MailType.DEFERRED || mail.mailType == MailType.DRAFT) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            launch {
                mailServerConnector.sendMailIsSeen(mailUIToMailServer(mail), user)
            }
            launch {
                val mailDb = mailDAO.getMailByPK(mail.mailID)
                /*if (mailDb.isNotEmpty()) {
                    mailDb[0].isRead = true
                    mailDAO.updateMail(mailUIToMailDB(mail))
                }*/
                if (mailDb != null) {
                    mailDb.isRead = true
                    mailDAO.updateMail(mailUIToMailDB(mail))
                }
            }
        }
    }

    suspend fun getMail(mailId: String): Mail? {
        val getMailJob = CoroutineScope(Dispatchers.IO).async {
            mailDAO.getMailByPK(mailId)
        }
        val mailListDb = getMailJob.await() ?: return null
        /*if (mailListDb.isEmpty()) {
        return null
    }
    return mailDBToMailUI(mailListDb[0])*/
        return mailDBToMailUI(mailListDb)
    }

    fun updateInFavourites(mail: Mail) {
        CoroutineScope(Dispatchers.IO).launch {
            val mailDb = mailDAO.getMailByPK(mail.mailID)
            if (mailDb != null) {
                mailDb.isInFavourites = mail.isInFavourites
                mailDAO.updateMail(mailUIToMailDB(mail))
            }

        }
    }

    fun saveDraft(mail: Mail, user: User): Flow<Boolean> {
        return flow {
                // save new draft
                val draft = mailServerConnector.saveDraft(mailUIToMailServer(mail), user)
                if (draft == null) {
                    emit(false)
                } else {
                    val mail = mailSrvToMailUI(draft)
                    mail.mailType = MailType.DRAFT
                    mailDAO.insertMail(mailUIToMailDB(mail))
                    emit(true)
                }
            if (!mail.mailID.isEmpty() && mailDAO.getMailByPK(mailID = mail.mailID) != null) {
                deleteMail(mail, user).collect()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun deleteMail(mail : Mail, user : User): Flow<Boolean> {
        return flow {
            coroutineScope {
                val serverJob = launch {
                    try {
                        mailServerConnector.deleteMail(mailUIToMailServer(mail), user)
                    } catch (exc : OperationException){
                        emit(false)
                    }
                }
                val dbJob = launch {
                        mailDAO.deleteMail(mailUIToMailDB(mail))
                }
                joinAll(serverJob, dbJob)
                emit(true)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun clearDBForTest(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            mailDAO.clear()
        }
    }

    suspend fun clearDB() {
        CoroutineScope(Dispatchers.IO).async {
            mailDAO.clear()
        }.await()
    }
}