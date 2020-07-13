package com.example.coctails.core

import android.app.Application
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.coctails.core.room.dao.RoomDataBase
import com.example.coctails.services.MyWorker
import com.example.coctails.utils.PublisherSubject
import com.facebook.stetho.Stetho
import java.util.concurrent.TimeUnit

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

        val workManager = WorkManager.getInstance(this)
        val request = PeriodicWorkRequest.Builder(MyWorker::class.java, 1, TimeUnit.DAYS).build()
        workManager.enqueueUniquePeriodicWork("Check Database Version", ExistingPeriodicWorkPolicy.REPLACE, request)

    }

    companion object {
        var instance: App? = null
            private set
    }
}