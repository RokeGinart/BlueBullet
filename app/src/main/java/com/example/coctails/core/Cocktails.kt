package com.example.coctails.core

import android.content.Context
import com.example.coctails.core.sharedpreference.PreferenceManager

object Cocktails : CoreApp {

    private val prefs = PreferenceManager()

    fun init(context: Context) {
        PreferenceManager.initializeShared(context)
    }

    override fun getPref(): PreferenceManager = prefs
}