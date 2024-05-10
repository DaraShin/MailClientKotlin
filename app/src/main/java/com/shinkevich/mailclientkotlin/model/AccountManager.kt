package com.shinkevich.mailclientkotlin.model

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountManager @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        val USER_PREFERENCES = "user_details"
        val USER_EMAIL_KEY = "username"
        val PASSWORD_KEY = "password"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        USER_PREFERENCES,
        Context.MODE_PRIVATE
    )

    fun needLogin(): Boolean {
        return !(sharedPreferences.contains(USER_EMAIL_KEY) && sharedPreferences.contains(
            PASSWORD_KEY
        ))
    }

    fun setActiveUser(userEmail: String, password: String) {
        val prefEditor = sharedPreferences.edit()
        prefEditor.putString(USER_EMAIL_KEY, userEmail)
        prefEditor.putString(PASSWORD_KEY, password)
        prefEditor.commit()
    }

    fun getActiveUser(): User? {
        if (needLogin()) {
            return null
        }
        return User(
            sharedPreferences.getString(USER_EMAIL_KEY, "")!!,
            sharedPreferences.getString(PASSWORD_KEY, "")!!
        )
    }

    fun logout() {
        val prefEditor = sharedPreferences.edit()
        prefEditor.remove(USER_EMAIL_KEY)
        prefEditor.remove(PASSWORD_KEY)
        prefEditor.commit()
    }
}