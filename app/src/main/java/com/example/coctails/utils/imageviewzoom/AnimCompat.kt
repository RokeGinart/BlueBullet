package com.example.coctails.utils.imageviewzoom

import android.os.Build
import android.view.View


class AnimCompat {
    private val SIXTY_FPS_INTERVAL = 1000 / 60L

    fun postOnAnimation(view: View, runnable: Runnable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postOnAnimation(runnable)
        } else {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL)
        }
    }
}