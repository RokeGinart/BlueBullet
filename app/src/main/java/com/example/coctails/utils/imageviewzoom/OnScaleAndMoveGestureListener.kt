package com.example.coctails.utils.imageviewzoom

import android.view.ScaleGestureDetector


interface OnScaleAndMoveGestureListener {

    fun onScaleBegin(detector: ScaleGestureDetector)

    fun onScaleEnd(
        detector: ScaleGestureDetector,
        moveDistanceFromX: Float,
        moveDistanceFromY: Float
    )

    fun onScaleAndMove(
        detector: ScaleGestureDetector,
        currentScale: Float,
        moveDistanceFromX: Float,
        moveDistanceFromY: Float
    )
}