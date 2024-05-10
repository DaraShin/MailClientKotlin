package com.shinkevich.mailclientkotlin.ui.maildetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinkevich.mailclientkotlin.model.repository.MailRepository
import com.shinkevich.mailclientkotlin.model.repository.UserRepository
import com.shinkevich.mailclientkotlin.ui.model.Mail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailDetailsViewModel @Inject constructor(
    private val mailRepos: MailRepository,
    private val userRepos: UserRepository,
) : ViewModel() {
    val mail = mutableStateOf<Mail?>(null)

    val isDeleting = mutableStateOf(false)
    val isDeleted = mutableStateOf(false)
    val navigateBack = mutableStateOf(false)

    fun getMail(mailId: String) {
        viewModelScope.launch {
            mail.value = mailRepos.getMail(mailId)
        }
    }

    fun updateInFavourites(mail: Mail) {
        mailRepos.updateInFavourites(mail)
    }

    fun deleteMail() {
        if (mail.value == null) {
            return
        }

        isDeleting.value = true

        val user = userRepos.getUser()!!

        viewModelScope.launch {
            mailRepos.deleteMail(mail.value!!, user).collect { deleteResult ->
                isDeleted.value = deleteResult
                navigateBack.value = deleteResult
                isDeleting.value = false
            }
        }
    }
}