package com.example.poke_wordle.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.poke_wordle.R


class WordleLetterView(context: Context): AppCompatTextView(context) {

    init {
        this.gravity = Gravity.CENTER
        this.textSize = 30F
        this.setTextColor(ContextCompat.getColor(context, R.color.black))
        this.background = ContextCompat.getDrawable(context, R.drawable.wordle_letter)
        this.typeface = Typeface.DEFAULT_BOLD

        this.setPadding(5, 5, 5, 5)
        val params = LinearLayout.LayoutParams(
            100, 120
        )
        params.setMargins(10,0, 10, 0)

        this.layoutParams = params
    }

    var isCorrect = false
        set(newValue) {
            field = newValue
            animateStateChange()
        }
    var isPresent = false
        set(newValue) {
            field = newValue
            animateStateChange()
        }
    var isChecked = false
        set(newValue) {
            field = newValue
            animateStateChange()
        }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 2)

        if (isChecked) { mergeDrawableStates(drawableState, STATE_CHECKED) }
        if (isPresent) { mergeDrawableStates(drawableState, STATE_PRESENT) }
        if (isCorrect) { mergeDrawableStates(drawableState, STATE_CORRECT) }

        return drawableState
    }

    private fun animateStateChange() {
        val anim = ValueAnimator.ofFloat(0f, 180f)
        anim.setTarget(this)
        anim.duration = 500
        anim.startDelay = 150 * (this.parent as ViewGroup).indexOfChild(this).toLong()
        anim.addUpdateListener {
            val degrees = it.animatedValue as Float
            this.rotationX = degrees
            if (degrees > 90f) {
                refreshDrawableState()
            }
        }
        anim.start()
    }

    companion object {
        private val STATE_CORRECT = intArrayOf(R.attr.state_letter_correct)
        private val STATE_PRESENT = intArrayOf(R.attr.state_letter_present)
        private val STATE_CHECKED = intArrayOf(R.attr.state_letter_checked)
    }

}