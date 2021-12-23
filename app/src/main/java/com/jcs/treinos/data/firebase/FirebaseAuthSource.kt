package com.jcs.treinos.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.jcs.treinos.data.models.User
import com.jcs.treinos.ui.AuthenticationActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signOut() = firebaseAuth.signOut()

    @ExperimentalCoroutinesApi
    fun getUser(): Flow<User?> =

        callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener {
                it.currentUser.let { user ->
                    trySend(
                        if (user != null) {
                            User(
                                user.uid,
                                user.displayName,
                                user.email,
                                user.photoUrl
                            )
                        } else null
                    )
                }
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }

    suspend fun authWithGoogle(idToken: String): Boolean =
        suspendCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d(AuthenticationActivity.TAG, "signInWithCredential:success")
                        continuation.resume(true)
                    } else {
                        Timber.w(
                            AuthenticationActivity.TAG,
                            "signInWithCredential:failure",
                            task.exception
                        )
                        continuation.resumeWith(Result.failure(task.exception!!))
                    }
                }
        }

}