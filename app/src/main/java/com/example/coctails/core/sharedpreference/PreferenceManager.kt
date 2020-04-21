package com.example.coctails.core.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager : IPref{

    override fun isFirstInit(): Boolean = sharedPreferences!!.getBoolean(PROP_FIRST_INIT, false)

    override fun setFirstInit(value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(PROP_FIRST_INIT, value)
        editor.apply()
    }

    companion object{
        private const val PREFERENCES_NAME = "Henkel_PREFS"
        private var sharedPreferences: SharedPreferences? = null
        const val PROP_FIRST_INIT: String = "PROP_FIRST_INIT"

        fun initializeShared(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }
}