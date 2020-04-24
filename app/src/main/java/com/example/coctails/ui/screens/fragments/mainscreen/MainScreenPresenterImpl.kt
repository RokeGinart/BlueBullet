package com.example.coctails.ui.screens.fragments.mainscreen

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainScreenPresenterImpl : MainScreenPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")

    override fun getCocktailsList(category: String) {
        addToDispose(
            App.instanse?.api?.getCocktailsByCategory(category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 -> screenView?.showAllCocktails(t1.drinks)})
    }

    override fun getSearchCocktailsList(name: String) {
        addToDispose(
            App.instanse?.api?.searchCocktails(name)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    screenView?.showSearchResult(t1?.drinks)
                })
    }

    override fun getFirebaseData() {
        myRef.child("shots").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<Cocktails>()

                dataSnapshot.children.forEach{
                    responseList.add(it.getValue(Cocktails::class.java)!!)
                }

                screenView?.showFirebaseResult(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}

