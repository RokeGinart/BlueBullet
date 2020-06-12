package com.example.coctails.ui.screens.fragments.favorites

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.FavoriteModel
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FavoritePresenterImpl : FavoritePresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")

    override fun getFavoriteList() {
        addToDispose(
            App.instance?.database?.favoriteDao()?.getAllFavorite()?.subscribeOn(Schedulers.io())
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
        addToDispose(
            App.instance?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{t1, t2 ->

                    t1?.forEach{
                        if(it.category?.category == category && it.id == id){
                            screenView?.getCocktail(it)
                        }
                    }
                }
        )
    }

    override fun setFavoriteStatus(cocktailId: Int, name: String, image: String, category: String, abv: Int, categoryName: String, favorite: Boolean) {
        val favoriteModel = FavoriteModel(cocktailId, name, image, category, abv, categoryName, favorite)

        addToDispose(
            App.instance?.database?.favoriteDao()?.getCocktail(cocktailId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instance?.database?.favoriteDao()?.insert(favoriteModel)
                    } else {
                        App.instance?.database?.favoriteDao()?.delete(t1)
                    }
                })
    }
}