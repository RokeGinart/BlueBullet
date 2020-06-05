package com.example.coctails.core

import android.app.Application
import androidx.room.Room
import com.example.coctails.core.room.dao.RoomDataBase
import com.example.coctails.network.ApiManager
import com.example.coctails.network.CocktailsAPI
import com.example.coctails.utils.PublisherSubject
import com.facebook.stetho.Stetho

class App : Application() {

    var api: CocktailsAPI? = null
        private set

    var database: RoomDataBase? = null
        private set

    var subject: PublisherSubject? = null
        private set

    override fun onCreate() {
        super.onCreate()
        Cocktails.init(this)

        instanse = this
        api = ApiManager()
        subject = PublisherSubject()
        Stetho.initializeWithDefaults(this)

        database = Room.databaseBuilder(this, RoomDataBase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }

    companion object {
        var instanse: App? = null
            private set
    }
}