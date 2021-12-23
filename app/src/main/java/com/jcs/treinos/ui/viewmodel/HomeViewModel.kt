package com.jcs.treinos.ui.viewmodel

import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.jcs.treinos.data.models.Workout
import com.jcs.treinos.data.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirestoreRepository) : ViewModel() {

    private val _workoutData = MutableLiveData<List<Workout?>>()
    val workout: LiveData<List<Workout?>> = _workoutData


    fun getWorkout() = viewModelScope.launch {
        _workoutData.value = repository.getWorkout()
    }

    fun addWorkout(name: String?, description: String?
    ) {
        viewModelScope.launch {
            repository.addWorkout(Workout(name, description, Timestamp.now()))
        }
    }
}