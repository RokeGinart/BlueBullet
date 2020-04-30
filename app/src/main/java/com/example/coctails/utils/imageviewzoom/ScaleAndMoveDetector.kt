package com.example.coctails.utils.imageviewzoom

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener

class ScaleAndMoveDetector(
    context: Context?,
    detectorListener: OnScaleAndMoveGestureListener?
) :
    OnScaleGestureListener {
    private var scaleStart = 1f
    private var LastX = 0f
    private var LastY = 0f
    private var moveDisFromX = 0f
    private var moveDisFromY = 0f
    private var mCurrentScale = 1f
    private val mDetector: ScaleGestureDetector
    private val mDetectorListener: OnScaleAndMoveGestureListener?
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val tempx = detector.focusX
        val tempy = detector.focusY
        moveDisFromX = tempx - LastX
        moveDisFromY = tempy - LastY
        mDetectorListener?.onScaleAndMove(
            detector,
            getCurrentScale(detector.scaleFactor),
            moveDisFromX,
            moveDisFromY
        )
        return false
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        LastX = detector.focusX
        LastY = detector.focusY
        moveDisFromX = 0f
        moveDisFromY = 0f
        scaleStart = detector.scaleFactor
        mDetectorListener?.onScaleBegin(detector)
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        mDetectorListener?.onScaleEnd(
            detector,
            moveDisFromX,
            moveDisFromY
        )
    }

    fun setStartScale(scaleFactor: Float) {
        scaleStart = scaleFactor
    }

    private fun getCurrentScale(scaleFactor: Float): Float {
        mCurrentScale = scaleFactor * (1f / scaleStart)
        return mCurrentScale
    }

    fun onTouchEvent(ev: MotionEvent?): Boolean {
        return mDetector.onTouchEvent(ev)
    }

    init {
        mDetector = ScaleGestureDetector(context, this)
        mDetectorListener = detectorListener
    }
}