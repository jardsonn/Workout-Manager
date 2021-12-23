package com.jcs.treinos.ui.adapter

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.jcs.treinos.R
import com.jcs.treinos.data.models.Workout
import com.jcs.treinos.databinding.WorkoutItemBinding
import kotlin.random.Random
import timber.log.Timber


class WorkoutAdapter : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WORKOUT_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            WorkoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size


    inner class WorkoutViewHolder(binding: WorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val workoutName = binding.tvNameWorkoutItem
        private val workoutIcon = binding.ivWorkoutItem
        private val icons: TypedArray =
            binding.root.context?.resources!!.obtainTypedArray(R.array.workout_icons)

        init {
            workoutIcon.setImageDrawable(randomIcon(icons))
        }

        fun bind(model: Workout) {
            workoutName.text = model.name
        }

    }

    private fun randomIcon(icons: TypedArray): Drawable? {
        return icons.getDrawable(Random.nextInt(0, icons.length()))
    }

    companion object {
        private val WORKOUT_COMPARATOR = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: Workout,
                newItem: Workout
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}