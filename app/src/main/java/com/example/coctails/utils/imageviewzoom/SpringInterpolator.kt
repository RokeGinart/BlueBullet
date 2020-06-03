package com.example.coctails.utils.imageviewzoom

import android.view.animation.Interpolator
import kotlin.math.pow
import kotlin.math.sin

class SpringInterpolator(private val factor: Float) : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return (2.0.pow(-10 * input.toDouble()) * sin((input - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat()
    }
}