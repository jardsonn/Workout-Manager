package com.jcs.treinos.data.repositories

import com.jcs.treinos.data.firebase.FirebaseFirestoreSource
import com.jcs.treinos.data.models.Workout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class FirestoreRepository @Inject constructor(private val source: FirebaseFirestoreSource) {

    suspend fun getWorkout() = source.getWorkout()

    suspend fun addWorkout(workout: Workout) = source.addWorkout(workout)
}