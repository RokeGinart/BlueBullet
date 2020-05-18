package com.example.coctails.ui.screens.fragments.favorites

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoritePresenterImpl : FavoritePresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")

    override fun getFavoriteList() {
        addToDispose(
            App.instanse?.database?.favoriteDao()?.getAllFavorite()?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1 ->
                    if (t1 != null) {
                        if (t1.isNotEmpty()) {
                            screenView?.showFavoriteList(t1)
                        } else {
                            screenView?.showFavoriteList(t1)
                            screenView?.showMessage()
                        }
                    }
                })
    }

    override fun getSelectedCocktail(category: String, id: Int) {
        myRef.child(category).child(id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    screenView?.getCocktail(dataSnapshot.getValue(Cocktails::class.java)!!)
                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
    }

    override fun setFavoriteStatus(cocktailId: Int, name: String, image: String, category: String, abv: Int, categoryName: String, favorite: Boolean) {
        val favoriteModel = FavoriteModel(cocktailId, name, image, category, abv, categoryName, favorite)

        addToDispose(
            App.instanse?.database?.favoriteDao()?.getCocktail(cocktailId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instanse?.database?.favoriteDao()?.insert(favoriteModel)
                    } else {
                        App.instanse?.database?.favoriteDao()?.delete(t1)
                    }
                })
    }
}