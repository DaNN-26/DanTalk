package com.example.dantalk.features.auth.sign_up.input_password.component

import com.arkivanov.decompose.ComponentContext
import com.example.core.firebase.auth.domain.repository.AuthRepository
import com.example.core.firebase.firestore.user.domain.repository.UserRepository
import com.example.domain.userdata.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DefaultInputPasswordComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigateToHome: () -> Unit,
    private val navigateBack: () -> Unit,
    private val currentUserData: UserData
) : ComponentContext by componentContext, InputPasswordComponent {


    private val scope = CoroutineScope(Dispatchers.IO)

//
//    private suspend fun checkPassword() = withContext(Dispatchers.Main) {
//        val validation = when {
//            state.value.password.isBlank() ->
//                InputPasswordValidation.EmptyPassword
//            state.value.password != state.value.repeatablePassword ->
//                InputPasswordValidation.NotMatchesPasswords
//
//            else -> InputPasswordValidation.Valid
//        }
//        _state.update { it.copy(validation = validation) }
//    }
//
//    private fun createUser(responseUserData: UserData) {
//        scope.launch {
//            checkPassword()
//            if(state.value.validation != InputPasswordValidation.Valid) return@launch
//            try {
//                _state.update { it.copy(isLoading = true) }
//                authRepository.createUser(
//                    email = responseUserData.email,
//                    password = state.value.password
//                )
//                userRepository.createUser(responseUserData)
//                _state.update { it.copy(
//                    isLoading = false,
//                    isSuccessful = true
//                ) }
//            } catch (e: Exception) {
//                val validation: InputPasswordValidation = when (e) {
//                    is FirebaseAuthWeakPasswordException ->
//                        InputPasswordValidation.PasswordIsTooShort
//
//                    is FirebaseNetworkException ->
//                        InputPasswordValidation.NetworkError
//
//                    else -> InputPasswordValidation.NetworkError
//                }
//                Log.d("CreateUser", e.toString())
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        validation = validation
//                    )
//                }
//            }
//        }
//    }

    companion object {
        const val INPUT_PASSWORD_COMPONENT = "INPUT_PASSWORD_COMPONENT"
    }
}