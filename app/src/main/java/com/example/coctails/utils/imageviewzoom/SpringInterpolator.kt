package com.example.coctails.utils.imageviewzoom

import android.view.animation.Interpolator

class SpringInterpolator(private val factor: Float) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return (Math.pow(
            2.0,
            -10 * input.toDouble()
        ) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }
}