package com.example.coctails.core

import android.content.Context
import com.example.coctails.core.sharedpreference.PreferenceManager

object Coctails : CoreApp {

    private val prefs = PreferenceManager()

    fun init(context: Context) {
        PreferenceManager.initializeShared(context)
    }

    override fun getPref(): PreferenceManager = prefs
}