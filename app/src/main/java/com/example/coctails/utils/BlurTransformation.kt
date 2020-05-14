package com.example.coctails.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BlurTransformation {
    private val BITMAP_SCALE = 0.1f
    private val BLUR_RADIUS = 7.5f

    fun blur(context: Context?, image: Bitmap): Bitmap? {
        val width = Math.round(image.width * BITMAP_SCALE)
        val height = Math.round(image.height * BITMAP_SCALE)
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(context)
        val theIntrinsic =
            ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(BLUR_RADIUS)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    /*   val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.second)
      val blurredBitmap: Bitmap? = BlurTransformation().blur(context, originalBitmap)
      categoryCocktails.background_image = BitmapDrawable(resources, blurredBitmap)*/

}