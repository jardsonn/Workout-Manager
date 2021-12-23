package com.jcs.treinos.data.repositories

import com.jcs.treinos.data.firebase.FirebaseAuthSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class AuthRepository @Inject constructor(private val source: FirebaseAuthSource){

    suspend fun authWithGoogle(idToken: String) = source.authWithGoogle(idToken)

    @ExperimentalCoroutinesApi
    suspend fun getUser() = source.getUser()

    fun signOut() = source.signOut()
}