package com.example.coctails.core

import android.app.Application
import com.example.coctails.network.ApiManager
import com.example.coctails.network.CocktailsAPI

class App : Application() {

    var api: CocktailsAPI? = null
        private set

    override fun onCreate() {
        super.onCreate()
        Cocktails.init(this)

        instanse = this
        api = ApiManager()
    }

    companion object {
        var instanse: App? = null
            private set
    }
}