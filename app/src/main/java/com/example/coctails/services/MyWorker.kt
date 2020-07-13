package com.example.coctails.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.coctails.R
import com.example.coctails.core.Cocktails
import com.example.coctails.ui.screens.activities.splash.SplashActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyWorker(context : Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var database = FirebaseDatabase.getInstance()
    private var databaseVersion = database.getReference("version")

    override fun doWork(): Result {
        databaseVersion.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (Cocktails.getPref().getVersion() != dataSnapshot.value) {
                    createNotification()
                } else {
                   Log.d("TAGS", "NO VERSION")
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })

        return Result.success()
    }

    private fun createNotification() {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifyIntent = Intent(applicationContext, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            applicationContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
            .setContentText(applicationContext.getString(R.string.notification))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }
}