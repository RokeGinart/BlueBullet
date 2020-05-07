package com.example.coctails.core

import android.app.Application
import androidx.room.Room
import com.example.coctails.core.room.dao.RoomFavoriteDB
import com.example.coctails.network.ApiManager
import com.example.coctails.network.CocktailsAPI
import com.facebook.stetho.Stetho

class App : Application() {

    var api: CocktailsAPI? = null
        private set

    var dbFavorite: RoomFavoriteDB? = null
        private set

    override fun onCreate() {
        super.onCreate()
        Cocktails.init(this)

        instanse = this
        api = ApiManager()
        Stetho.initializeWithDefaults(this)

        dbFavorite = Room.databaseBuilder(this, RoomFavoriteDB::class.java, "databaseFavorite")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }

    companion object {
        var instanse: App? = null
            private set
    }
}