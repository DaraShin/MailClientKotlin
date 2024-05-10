package com.shinkevich.mailclientkotlin.ui.maillist

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinkevich.mailclientkotlin.R
import com.shinkevich.mailclientkotlin.model.MailType
import com.shinkevich.mailclientkotlin.model.repository.MailRepository
import com.shinkevich.mailclientkotlin.model.repository.UserRepository
import com.shinkevich.mailclientkotlin.ui.model.Mail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailListViewModel @Inject constructor(
    private val mailRepos: MailRepository,
    private val userRepos : UserRepository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    val topBarTitle: MutableState<String> =
        mutableStateOf(applicationContext.getString(R.string.incoming))

    fun getMailList(mailType: MailType): MutableState<List<Mail>> {
        val user = userRepos.getUser()!!
        val mailListState = mutableStateOf<List<Mail>>(listOf())
        viewModelScope.launch {
            mailRepos.getMailsByType(mailType, user)
                .collect { mailList ->
                    mailListState.value = mailList.sortedByDescending { it.date}
                }
        }
        return mailListState
    }

    fun setAppBarTitle(route: String) {
        if (topBarTitleMap.containsKey(route)) {
            topBarTitle.value = topBarTitleMap[route]!!
        }
    }

    private val topBarTitleMap = hashMapOf<String, String>(
        NavigationItem.Incoming.route to applicationContext.getString(R.string.incoming),
        NavigationItem.Sent.route to applicationContext.getString(R.string.sent),
        NavigationItem.Deferred.route to applicationContext.getString(R.string.deferred),
        NavigationItem.Draft.route to applicationContext.getString(R.string.drafts),
        NavigationItem.Favourite.route to applicationContext.getString(R.string.favourites),
        NavigationItem.Spam.route to applicationContext.getString(R.string.spam)
    )

    fun setMailIsSeen(mail : Mail){
        val user = userRepos.getUser()!!
        mail.isRead = true
        mailRepos.setMailIsSeen(mail, user)
    }

    fun updateInFavourites(mail : Mail){
        mailRepos.updateInFavourites(mail)
    }

    fun getUserEmail() : String {
        return userRepos.getUser()!!.email
    }

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            userRepos.logout()
            mailRepos.clearDB()
        }
    }

}