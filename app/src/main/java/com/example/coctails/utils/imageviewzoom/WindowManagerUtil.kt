package com.example.coctails.utils.imageviewzoom

import android.os.Build
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.annotation.NonNull


class WindowManagerUtil {

    @Synchronized
    fun removeViewSafety(@NonNull windowManager: WindowManager?, @NonNull viewNeedRemove: View?) {
        if (windowManager == null || viewNeedRemove == null) return
        if (Looper.myLooper() != Looper.getMainLooper()) { // Current thread is not the UI/Main thread
            return
        }
        // Check  is the view attaching
        if (isAttachedToWindow(viewNeedRemove)) {
            try {
                windowManager.removeView(viewNeedRemove)
            } catch (e: Exception) {
            }
            return
        }
        try {
            windowManager.removeView(viewNeedRemove)
        } catch (e: Exception) {
        }
    }


    @Synchronized
    fun addViewSafety(@NonNull windowManager: WindowManager?, @NonNull viewNeedAdd: View?, @NonNull params: WindowManager.LayoutParams?) {
        if (windowManager == null || viewNeedAdd == null || params == null) return
        if (Looper.myLooper() != Looper.getMainLooper()) { // Current thread is not the UI/Main thread
            return
        }
        if (!isAttachedToWindow(viewNeedAdd)) {
            try {
                windowManager.addView(viewNeedAdd, params)
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Check  view is  attach to window
     */
    fun isAttachedToWindow(view: View?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view?.isAttachedToWindow!!
        } else {
            view?.windowToken != null
        }
    }
}