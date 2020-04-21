package com.example.coctails.core

import com.example.coctails.core.sharedpreference.PreferenceManager

interface CoreApp {

    fun getPref(): PreferenceManager
}