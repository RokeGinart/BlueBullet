package com.example.coctails.utils.imageviewzoom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.animation.Interpolator
import android.widget.ImageView
import com.example.coctails.R
import java.lang.ref.WeakReference
import kotlin.math.pow
import kotlin.math.sqrt


class ZoomViewAttacher(imageView: ImageView?) : OnTouchListener {
    private var mWindowManager: WindowManager? = null
    private var mWindowLayoutParams: WindowManager.LayoutParams? = null
    /**
     * drag imageview
     */
    private var mZoomIV: ImageView? = null
    /**
     * drag image Bitmap
     */
    private var mZoomBitmap: Bitmap? = null
    private var mOffsetToTop = 0
    private var mOffsetToLeft = 0
    /**
     * is zoomable
     */
    private var mZoomEnabled = true
    private var mScaleGestureDetector: ScaleAndMoveDetector? = null
    private var ZOOM_DURATION = 1000
    private var mWindowLayout: View? = null
    private val mSuppMatrix = Matrix()
    private val mBaseMatrix = Matrix()
    private val mDrawMatrix = Matrix()
    private var mAnimInterpolator: Interpolator = SpringInterpolator(1f)
    private var hasInterruptParentNotToHandleTouchEvent = false
    private var mImageView: WeakReference<ImageView?>? = null


    @SuppressLint("ClickableViewAccessibility")
    private fun attachImageView(imageView: ImageView?) {
        if (imageView == null) {
            throw NullPointerException("imageview is null")
        }
        mImageView = WeakReference(imageView)
        imageView.setOnTouchListener(this)
        initGestureDetector()
    }

    @Synchronized
    fun initGestureDetector() {
        mScaleGestureDetector = ScaleAndMoveDetector(
            imageViewS!!.context,
            object : OnScaleAndMoveGestureListener {
                override fun onScaleAndMove(
                    detector: ScaleGestureDetector,
                    currentScale: Float,
                    moveDistanceFromX: Float,
                    moveDistanceFromY: Float
                ) {
                    var currentScale = currentScale
                    if (mZoomIV == null) {
                        mScaleGestureDetector!!.setStartScale(detector.scaleFactor)
                        return
                    }
                    mSuppMatrix.reset()
                    val centerX = mOffsetToLeft + imageViewS!!.width / 2.0f
                    val centerY = mOffsetToTop + imageViewS!!.height / 2.0f
                    if (currentScale >= 1.0) {
                        mSuppMatrix.postScale(currentScale, currentScale, centerX, centerY)
                    } else {
                        currentScale = 1f
                        mSuppMatrix.postScale(1f, 1f, centerX, centerY)
                    }
                    mSuppMatrix.postTranslate(moveDistanceFromX, moveDistanceFromY)
                    if (mWindowLayout != null) {
                        if (currentScale >= 1.0) {
                            if (currentScale > 3.0) {
                                mWindowLayout!!.setBackgroundColor(
                                    Color.argb(
                                        200,
                                        0,
                                        0,
                                        0
                                    )
                                )
                            } else {
                                mWindowLayout!!.setBackgroundColor(
                                    Color.argb(
                                        (200 * (currentScale - 1.0) / 2.0f).toInt(),
                                        0,
                                        0,
                                        0
                                    )
                                )
                            }
                        } else {
                            mWindowLayout!!.setBackgroundColor(
                                Color.argb(
                                    0,
                                    0,
                                    0,
                                    0
                                )
                            )
                        }
                    }
                    setImageViewMatrix(drawMatrix)
                }

                override fun onScaleBegin(detector: ScaleGestureDetector) {
                    if (mZoomBitmap == null) {
                        imageViewS!!.isDrawingCacheEnabled = true
                        mZoomBitmap = Bitmap.createBitmap(imageViewS!!.drawingCache)
                        imageViewS!!.destroyDrawingCache()
                    }
                    createZoomImage(mZoomBitmap, mOffsetToLeft, mOffsetToTop)
                }

                override fun onScaleEnd(
                    detector: ScaleGestureDetector,
                    moveDistanceFromX: Float,
                    moveDistanceFromY: Float
                ) {
                    onReleaseZoom(moveDistanceFromX, moveDistanceFromY)
                }
            })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clean() {
        if (null != mImageView) {
            if (null != mImageView!!.get()) {
                mImageView!!.get()!!.setOnTouchListener(null)
            }
            mImageView = null
        }
        mWindowLayoutParams = null
        mWindowLayout = null
        mWindowManager = null
        mScaleGestureDetector = null
        recycleZoomBitmap()
    }

    /**
     * On  release zoom
     */
    private fun onReleaseZoom(disx: Float, disy: Float) {
        if (imageViewS == null) return
        if (mZoomIV != null) {
            val centerx = mOffsetToLeft + mZoomBitmap!!.width / 2.0f
            val centery = mOffsetToTop + mZoomBitmap!!.height / 2.0f
            mZoomIV!!.post(
                AnimatedZoomRunnable(
                    scale, 1f,
                    centerx, centery, disx, disy
                )
            )
        }
    }

    /**
     * remove zoom  layout
     */
    private fun removeZoomImage() {
        hasInterruptParentNotToHandleTouchEvent = false
        if (mZoomIV != null) {
            mZoomIV!!.visibility = View.INVISIBLE
            if (mWindowManager != null && mWindowLayout != null) {
                WindowManagerUtil().removeViewSafety(mWindowManager, mWindowLayout)
            }
            mZoomIV = null
            recycleZoomBitmap()
        }
    }

    private fun recycleZoomBitmap() {
        if (null != mZoomBitmap && !mZoomBitmap!!.isRecycled) {
            mZoomBitmap!!.recycle()
            mZoomBitmap = null
        }
    }

    /**
     * @param bitmap
     * @param mOffsetToLeft
     * @param mOffsetToTop
     */
    @SuppressLint("InflateParams")
    @Synchronized
    private fun createZoomImage(
        bitmap: Bitmap?,
        mOffsetToLeft: Int,
        mOffsetToTop: Int
    ) {
        if (imageViewS == null) return
        if (mWindowLayoutParams == null || mWindowLayout == null) {
            mWindowLayoutParams = WindowManager.LayoutParams()
            mWindowLayoutParams!!.format = PixelFormat.RGBA_8888
            mWindowLayoutParams!!.alpha = 1f
            mWindowLayoutParams!!.width = WindowManager.LayoutParams.MATCH_PARENT
            mWindowLayoutParams!!.height = WindowManager.LayoutParams.MATCH_PARENT
            mWindowLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            mWindowLayout = LayoutInflater.from(imageViewS!!.context)
                .inflate(R.layout.zoom_image, null)
        }
        mWindowLayout!!.isClickable = true
        mZoomIV =
            mWindowLayout!!.findViewById<View>(R.id.iv_zoominpic) as ImageView
        mZoomIV!!.visibility = View.VISIBLE
        mZoomIV!!.setImageBitmap(bitmap)
        mBaseMatrix.reset()
        mSuppMatrix.reset()
        mBaseMatrix.postTranslate(
            mOffsetToLeft.toFloat(),
            mOffsetToTop.toFloat()
        )
        mDrawMatrix.set(mBaseMatrix)
        mZoomIV!!.scaleType = ImageView.ScaleType.MATRIX
        setImageViewMatrix(mDrawMatrix)
        mWindowLayout!!.viewTreeObserver
            .addOnGlobalLayoutListener {
                if (mImageView!!.get() != null) {
                    imageViewS!!.postDelayed({ imageViewS!!.visibility = View.INVISIBLE }, 300)
                }
            }
        Log.e("ivd", "show drag image")
        if (mWindowManager == null) {
            mWindowManager =
                imageViewS!!.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        WindowManagerUtil().addViewSafety(mWindowManager, mWindowLayout, mWindowLayoutParams)
    }

    val drawMatrix: Matrix
        get() {
            mDrawMatrix.set(mBaseMatrix)
            mDrawMatrix.postConcat(mSuppMatrix)
            return mDrawMatrix
        }

    private fun setImageViewMatrix(matrix: Matrix) {
        if (null != mZoomIV) {
            mZoomIV!!.imageMatrix = matrix
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean { //
        if (mZoomEnabled) {
            if (event.pointerCount >= 2) { //if touch by more than two finger  ,handle by itself
                if (!hasInterruptParentNotToHandleTouchEvent) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    hasInterruptParentNotToHandleTouchEvent = true
                }
            } else {
                hasInterruptParentNotToHandleTouchEvent = false
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mOffsetToTop = (event.rawY - event.y).toInt()
                mOffsetToLeft = (event.rawX - event.x).toInt()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_CANCEL -> {
            }
            MotionEvent.ACTION_OUTSIDE -> {
            }
            else -> {
            }
        }
        var handled = false
        if (mZoomEnabled && null != mScaleGestureDetector && mScaleGestureDetector!!.onTouchEvent(
                event
            )
        ) {
            handled = true
        }
        if (view.onTouchEvent(event)) {
            handled = true
        }
        return handled
    }

    val imageViewS: ImageView?
        get() = if (mImageView != null) mImageView!!.get() else {
            clean()
            null
        }

    //FloatMath
    private val scale: Float
        get() =//FloatMath
            sqrt(
                getValue(
                    mSuppMatrix,
                    Matrix.MSCALE_X
                ).toDouble().pow(2.0).toFloat() + getValue(
                    mSuppMatrix,
                    Matrix.MSKEW_Y
                ).toDouble().pow(2.0)
            ).toFloat()

    private val mMatrixValues = FloatArray(9)
    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    private inner class AnimatedZoomRunnable(
        currentZoom: Float,
        targetZoom: Float,
        private val mFocalX: Float,
        private val mFocalY: Float,
        translateDistanceX: Float,
        translateDistanceY: Float
    ) :
        Runnable {
        private val mStartTime: Long = System.currentTimeMillis()
        private val mZoomStartScale: Float = currentZoom
        private val mZoomEndScale: Float = targetZoom
        private val mTranslateDistanceX: Float = translateDistanceX
        private val mTranslateDistanceY: Float = translateDistanceY
        override fun run() {
            val imageView = mZoomIV ?: return
            val time =
                1f * (System.currentTimeMillis() - mStartTime) / ZOOM_DURATION
            val t = mAnimInterpolator.getInterpolation(time)
            val scales = mZoomStartScale + t * (mZoomEndScale - mZoomStartScale)
            mSuppMatrix.reset()
            mSuppMatrix.postScale(scales, scales, mFocalX, mFocalY)
            val x = mTranslateDistanceX + t * (0 - mTranslateDistanceX)
            val y = mTranslateDistanceY + t * (0 - mTranslateDistanceY)
            mSuppMatrix.postTranslate(x, y)
            setImageViewMatrix(drawMatrix)
            var stopTime = 1f
            if (mAnimInterpolator is SpringInterpolator) {
                stopTime = 0.8f
            }
            if (time < stopTime) {
                AnimCompat().postOnAnimation(imageView, this)
            } else {
                imageView.post {
                    imageViewS?.visibility = View.VISIBLE
                    removeZoomImage()
                }
            }
        }

        init {
            mWindowLayout!!.setBackgroundColor(Color.argb(0, 0, 0, 0))
        }
    }

    init {
        attachImageView(imageView)
    }
}