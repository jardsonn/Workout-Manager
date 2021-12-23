package com.jcs.treinos.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jcs.treinos.data.models.Workout
import com.jcs.treinos.utils.USER_COLLECTIONS
import com.jcs.treinos.utils.WORKOUT_COLLECTIONS
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseFirestoreSource @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) :
    FirestoreDataSource {

    private val userId by lazy {
        FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun userRef() = userId?.let { firebaseFirestore.collection(USER_COLLECTIONS).document(it) }

    private  fun workoutRef() = userRef()?.collection(WORKOUT_COLLECTIONS)

    override suspend fun getWorkout(): List<Workout?> =
        suspendCoroutine { continuation ->
            workoutRef()?.get()
                ?.addOnSuccessListener { querySnapshot ->
                    val workouts = mutableListOf<Workout?>()
                    for (document in querySnapshot.documents) {
                        document.toObject(Workout::class.java).run {
                            workouts.add(this)
                        }
                    }
                    continuation.resumeWith(Result.success(workouts))
                }
            workoutRef()?.get()
                ?.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }

    override suspend fun addWorkout(workout: Workout): Boolean =
        suspendCoroutine { continuation ->
            workoutRef()
                ?.add(workout)
                ?.addOnSuccessListener { _ ->
                    Timber.d("Novo treino adicionado")
                    continuation.resume(true)
                }
                ?.addOnFailureListener {
                    Timber.d("Erro ao adiconar treino: $it")
                    continuation.resumeWith(Result.failure(it))
                }

        }
}


