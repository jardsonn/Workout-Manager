package com.jcs.treinos.ui.component

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.jcs.treinos.R
import com.jcs.treinos.databinding.DialogCreateWorkoutBinding
import com.jcs.treinos.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.processNextEventInCurrentThread

class CreateWorkoutDialog : DialogFragment() {

    private lateinit var viewModel: HomeViewModel
    private var positiveClickListener: OnDialogButtonsListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            val binding = DialogCreateWorkoutBinding.inflate(LayoutInflater.from(this.context))
            alertDialog.setView(binding.root)
            initDialog(binding)
            val dialog = alertDialog.create()
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog
        } ?: throw IllegalStateException("This activity is null")
    }

    private fun initDialog(binding: DialogCreateWorkoutBinding) {
        val editName = binding.etNameDialog
        val editDescription = binding.etDescriptionDialog
        val btnSave = binding.btnSaveDialog
        val btnCancel = binding.btnCancelDialog
        btnSave.setOnClickListener {
            val name = editName.text.toString().trim()
            if (name.isNotEmpty()) {
                viewModel.addWorkout(
                    name,
                    editDescription.text.toString().trim()
                )
                positiveClickListener?.onClick(SaveState.Success(getString(R.string.save_success)))
                dialog?.dismiss()
            }else{
                positiveClickListener?.onClick(SaveState.Error(getString(R.string.empty_name)))
            }

        }

        btnCancel.setOnClickListener { dialog?.dismiss() }
    }

    private fun setViewModel(viewModel: HomeViewModel) {
        this.viewModel = viewModel
    }

    private fun setOnDialogButtonsListener(listener: OnDialogButtonsListener) {
        this.positiveClickListener = listener
    }

    fun setOnDialogButtonsListener(l: (saveState: SaveState) -> Unit) {
        setOnDialogButtonsListener(object : OnDialogButtonsListener {
            override fun onClick(saveState: SaveState) {
                l.invoke(saveState)
            }
        })
    }


    sealed class SaveState{
        data class Success(val message: String): SaveState()
        data class Error(val message: String): SaveState()
    }


    interface OnDialogButtonsListener {
        fun onClick(saveState: SaveState)
    }

    companion object {
        fun getInstance(viewModel: HomeViewModel) = CreateWorkoutDialog().apply {
            setViewModel(viewModel)
        }
    }
}