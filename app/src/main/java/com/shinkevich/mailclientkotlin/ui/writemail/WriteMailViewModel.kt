package com.shinkevich.mailclientkotlin.ui.writemail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinkevich.mailclientkotlin.model.repository.MailRepository
import com.shinkevich.mailclientkotlin.model.repository.UserRepository
import com.shinkevich.mailclientkotlin.ui.model.Mail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteMailViewModel @Inject constructor(
    private val mailRepository: MailRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val recipientEmail = mutableStateOf("")
    val subject = mutableStateOf("")
    val text = mutableStateOf("")

    var mail = Mail()

    val isSending = mutableStateOf(false)
    val isSentStatus = mutableStateOf(false)
    val isSavingDraft = mutableStateOf(false)
    val isDraftSavedStatus = mutableStateOf(false)

    val navigateBack = mutableStateOf(false)
    val showErrorToast = mutableStateOf(false)

    var needToInitDrafts = true

    fun getCurrentUserEmail(): String {
        return "dasha.shin33@gmail.com"
    }

    fun sendMail() {
        isSending.value = true

        val user = userRepository.getUser()!!
        mail.authorEmail = user.email

        viewModelScope.launch {
            if(mail.mailID.isNotEmpty()){
                mailRepository.deleteMail(mail, user).collect()
            }
            mailRepository.sendMail(mail, user).collect { isSent ->
                isSending.value = false
                isSentStatus.value = isSent
                navigateBack.value = isSent
                showErrorToast.value = !isSent
            }
        }
    }

    private fun fillMailData() : Mail {
        if(recipientEmail.value.isNotBlank()){
            mail.recipients = listOf(recipientEmail.value)
        }
        mail.topic = subject.value
        mail.text = text.value
        return mail
    }

    fun saveDraft() {
        isSavingDraft.value  = true
        val mail = fillMailData()
        val user = userRepository.getUser()!!
        viewModelScope.launch {
            mailRepository.saveDraft(mail, user).collect{ saveDraftResult ->
                isDraftSavedStatus.value = saveDraftResult
                isSavingDraft.value  = false
                navigateBack.value = saveDraftResult
                showErrorToast.value = !saveDraftResult

            }
        }
    }

    fun initAsDraft(mailId : String){
        viewModelScope.launch {
            val draft = mailRepository.getMail(mailId)
            if(draft != null){
                subject.value = draft.topic
                text.value = draft.text
                if(draft.recipients.isNotEmpty()){
                    recipientEmail.value = draft.recipients[0]
                }
                mail = draft
                needToInitDrafts = false
            }
        }
    }
}