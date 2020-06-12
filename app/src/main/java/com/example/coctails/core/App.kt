package com.example.coctails.core

import android.app.Application
import androidx.room.Room
import com.example.coctails.core.room.dao.RoomDataBase
import com.example.coctails.utils.PublisherSubject
import com.facebook.stetho.Stetho

class App : Application() {

    var database: RoomDataBase? = null
        private set

    var subject: PublisherSubject? = null
        private set

    override fun onCreate() {
        super.onCreate()
        Cocktails.init(this)

        instance = this
        subject = PublisherSubject()
        Stetho.initializeWithDefaults(this)

        database = Room.databaseBuilder(this, RoomDataBase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }

    companion object {
        var instance: App? = null
            private set
    }
}