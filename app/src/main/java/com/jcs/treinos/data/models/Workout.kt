package com.jcs.treinos.data.models

import com.google.firebase.Timestamp

data class Workout(
    val name: String? = null,
    val description: String? = null,
    val date: Timestamp? = null
)