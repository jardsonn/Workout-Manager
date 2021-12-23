package com.jcs.treinos.data.firebase

import com.jcs.treinos.data.models.Workout

interface FirestoreDataSource {
    suspend fun getWorkout(): List<Workout?>
    suspend fun addWorkout(workout: Workout): Boolean
}