package com.shinkevich.mailclientkotlin.model.repository

import com.shinkevich.mailclientkotlin.model.AccountManager
import com.shinkevich.mailclientkotlin.model.User
import com.shinkevich.mailclientkotlin.model.network.auth.ServerAuth
import com.shinkevich.mailclientkotlin.model.network.exceptions.NoConnectionException
import com.sun.mail.util.MailConnectException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val serverAuth: ServerAuth,
    private val accountManager: AccountManager
) {

    suspend fun checkAuthenticationData(email: String, password: String): Boolean {
        val authResult = CoroutineScope(Dispatchers.IO).async {
            try {
                serverAuth.checkAuthenticationData(email, password)
            } catch (exc : MailConnectException){
                throw  NoConnectionException()
            } /*catch (exc : java.lang.Exception){
                throw OperationException()
            }*/
        }
        return authResult.await()
    }
    /*fun checkAuthenticationData(email: String, password: String): Flow<Boolean> {
        return flow {
            emit(serverAuth.checkAuthenticationData(email, password))
        }.catch { exc ->
            if (exc is MailConnectException) {
                throw NoConnectionException()
            } else {
                throw OperationException()
            }
        }
            .flowOn(Dispatchers.IO)
    }*/

    fun getUser(): User? {
        //return User("dasha.shin33@gmail.com", "oukkjhfkcystjeyv")
        return if (!accountManager.needLogin()){
            accountManager.getActiveUser()
        }else {
            null
        }
    }

    fun saveUser(email: String, password: String) {
        accountManager.setActiveUser(email, password)
    }

    fun logout() {
        accountManager.logout()
    }
}