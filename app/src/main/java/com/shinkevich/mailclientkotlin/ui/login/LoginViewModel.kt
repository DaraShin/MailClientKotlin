package com.shinkevich.mailclientkotlin.ui.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinkevich.mailclientkotlin.model.network.exceptions.NoConnectionException
import com.shinkevich.mailclientkotlin.model.repository.UserRepository
import com.shinkevich.mailclientkotlin.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val navigateOnAuthenticated = mutableStateOf(false)

    val showErrorMessage = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    var emailValid = mutableStateOf(true)
    var emailValidationError = mutableStateOf("")
    var passwordValid = mutableStateOf(true)
    var passwordValidationError = mutableStateOf("")

    var emailState = mutableStateOf("")
    var passwordState = mutableStateOf("")
    var passwordVisible = mutableStateOf(false)
    var loading = mutableStateOf(false)

    init {
        navigateOnAuthenticated.value = userRepository.getUser() != null
    }

    fun tryAuthenticate(email: String, password: String) {
        loading.value = true
        viewModelScope.launch{
            try {
                val authSuccessful = userRepository.checkAuthenticationData(email, password)
                if (authSuccessful) {
                    userRepository.saveUser(email, password)
                } else {
                    showErrorMessage.value = true
                    errorMessage.value = context.getString(R.string.invalid_email_or_password)
                }
                navigateOnAuthenticated.value = authSuccessful
            } catch (exc: java.lang.Exception) {
                navigateOnAuthenticated.value = false
                showErrorMessage.value = true
                errorMessage.value = if (exc is NoConnectionException) {
                    context.getString(R.string.no_connection)
                } else {
                    context.getString(R.string.authentication_error)
                }
            } finally {
                loading.value = false
            }
        }
    }

    fun validateEmail(email: String) {
        if (email.isBlank()) {
            emailValid.value = false
            emailValidationError.value = context.getString(R.string.required_field)
        } else {
            emailValid.value = true
        }
    }

    fun validatePassword(password: String) {
        if (password.isBlank()) {
            passwordValid.value = false
            passwordValidationError.value = context.getString(R.string.required_field)
        } else {
            passwordValid.value = true
        }
    }

    private fun validEmailFormat(value: String): Boolean {
        return value.matches(Regex("^[\\w\\._-]+@(\\w)+\\.(\\w)+\$"))
    }
}