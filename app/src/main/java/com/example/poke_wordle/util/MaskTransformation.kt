package com.example.poke_wordle.util

import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.squareup.picasso.Transformation

class MaskTransformation(context: Context, maskId: Int) : Transformation {
    private val mContext: Context
    private val mMaskId: Int

    companion object {
        private val mMaskingPaint: Paint = Paint()

        init {
            mMaskingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        }
    }

    override fun transform(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val mask = getMaskDrawable(mContext, mMaskId)
        val canvas = Canvas(result)
        mask.setBounds(0, 0, width, height)
        mask.draw(canvas)
        canvas.drawBitmap(source, 0.toFloat(), 0.toFloat(), mMaskingPaint)
        source.recycle()
        return result
    }

    override fun key(): String {
        return "MaskTransformation(maskId=" + mContext.resources.getResourceEntryName(mMaskId)
            .toString() + ")"
    }

    private fun getMaskDrawable(
        context: Context?,
        maskId: Int
    ): Drawable {
        return context?.let { ContextCompat.getDrawable(it, maskId) }
            ?: throw IllegalArgumentException("maskId is invalid")
    }

    /**
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get
     * the cache with the old mask. Because getId() return the same values if using the
     * same make file name. If you have a good idea please tell us, thanks.
     */
    init {
        mContext = context.applicationContext
        mMaskId = maskId
    }
}