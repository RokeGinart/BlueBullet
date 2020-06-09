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

    override fun isFirstEnter(): Boolean = sharedPreferences!!.getBoolean(SP_ENTER, true)

    override fun setFirstEnter(firstEnter: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(SP_ENTER, firstEnter)
        editor.apply()
    }

    override fun isAdult(): Boolean = sharedPreferences!!.getBoolean(SP_ADULT, false)

    override fun setAdult(adult: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(SP_ADULT, adult)
        editor.apply()
    }

    override fun isAdultSecondCheck(): Boolean = sharedPreferences!!.getBoolean(SP_ADULT_SECOND, false)

    override fun setAdultSecondCheck(adultSecond: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(SP_ADULT_SECOND, adultSecond)
        editor.apply()
    }

    override fun getVersion() : Long = sharedPreferences!!.getLong(SP_VERSION, 0L)

    override fun setVersion(version: Long) {
        val editor = sharedPreferences!!.edit()
        editor.putLong(SP_VERSION, version)
        editor.apply()
    }

    companion object{
        private const val PREFERENCES_NAME = "Henkel_PREFS"
        private var sharedPreferences: SharedPreferences? = null
        const val PROP_FIRST_INIT: String = "PROP_FIRST_INIT"

        const val SP_ENTER = "SP_ENTER"
        const val SP_VERSION = "SP_VERSION"
        const val SP_ADULT = "SP_ADULT"
        const val SP_ADULT_SECOND = "SP_ADULT_SECOND"

        fun initializeShared(context: Context) {
            sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }
}