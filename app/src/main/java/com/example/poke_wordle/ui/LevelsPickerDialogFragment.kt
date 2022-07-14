package com.example.poke_wordle.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.R
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LevelsPickerDialogFragment(val onSelect: () -> Unit) : DialogFragment() {
    private val wordleViewModel by sharedViewModel<PokeWordleViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_level)
                .setItems(R.array.levels) { _, which ->
                    val level = resources.getStringArray(R.array.levels)[which]
                    wordleViewModel.createNewGame(level)
                    onSelect()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}