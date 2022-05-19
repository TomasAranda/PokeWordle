package com.example.poke_wordle

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PokemonImageHintDialog(val setImage: (ImageView) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageView = ImageView(requireContext())
        setImage(imageView)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Este es tu Pokemon")
            builder.setView(imageView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }




}