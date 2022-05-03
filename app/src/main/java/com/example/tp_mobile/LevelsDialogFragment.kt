package com.example.tp_mobile

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class LevelsDialogFragment(val onSelect: (String) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_level)
                .setItems(R.array.levels) { _, which ->
                    onSelect(resources.getStringArray(R.array.levels)[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}