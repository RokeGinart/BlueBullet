package com.example.coctails.ui.screens.activities.splash

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.Cocktails
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashPresenterImpl : SplashPresenter() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.getReference("drink")
    private var databaseVersion = database.getReference("version")

    override fun downloadData() {
        databaseVersion.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
               if(Cocktails.getPref().getVersion() != dataSnapshot.value){
                   Cocktails.getPref().setVersion(dataSnapshot.value as Long)
                   startDownloading()
               } else {
                   screenView?.message()
               }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun startDownloading(){
        addToDispose(
            App.instanse?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe { t1, t2 ->
                    if(t1 != null){
                        App.instanse?.database?.cocktailFB()?.deleteAllData(t1)
                        getAllCocktails()
                    }
                }
        )
    }

    private fun getAllCocktails() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {
                    it.children.forEach { cocktails ->
                        App.instanse?.database?.cocktailFB()
                            ?.insert(cocktails.getValue(CocktailFirebaseData::class.java)!!)
                    }
                }

                screenView?.message()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}