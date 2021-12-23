package com.jcs.treinos.data.models

import android.net.Uri
import java.io.Serializable

data class User(
    val id: String?,
    val name: String?,
    val email: String?,
    val photoUrl: Uri?
    )
