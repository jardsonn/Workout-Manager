package com.jcs.treinos.ui.viewmodel

import androidx.lifecycle.*
import com.jcs.treinos.data.models.User
import com.jcs.treinos.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableLiveData<SignInWithGoogleState>()
    val authState: LiveData<SignInWithGoogleState> get() = _authState
    private val _userState = MutableLiveData<User>()
    val userState: LiveData<User> get() = _userState

    fun signOut() = repository.signOut()

     @ExperimentalCoroutinesApi
     suspend fun getUser() = repository.getUser()

    fun authWithGoogle(idToken: String) {
        viewModelScope.launch {
            val hasAuthenticated = repository.authWithGoogle(idToken)
            _authState.value =
                if (hasAuthenticated)
                    SignInWithGoogleState.Success
                 else SignInWithGoogleState.Error
        }
    }

    sealed class SignInWithGoogleState {
        object Success : SignInWithGoogleState()
        object Error : SignInWithGoogleState()
    }

//    sealed class UserState {
//        data class AuthenticatedUser(val user:
//                                     LiveData<User?>) : UserState()
//    }

}