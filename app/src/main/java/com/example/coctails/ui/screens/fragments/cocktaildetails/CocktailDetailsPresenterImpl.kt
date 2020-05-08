package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.FavoriteModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CocktailDetailsPresenterImpl : CocktailDetailsPresenter() {

    override fun getFavorite(cocktailId: Int, category: String) {
        addToDispose(
            App.instanse?.dbFavorite?.favoriteDao()?.getCocktail(cocktailId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if(t1 != null) {
                        if (t1.favorite) {
                            screenView?.showFavorite(true)
                        } else {
                            screenView?.showFavorite(false)
                        }
                    }
                })
    }

    override fun saveCocktailToFavorite(
        cocktailId: Int,
        name: String,
        image: String,
        category: String,
        abv: Int,
        categoryName: String,
        favorite: Boolean
    ) {
        val favoriteModel = FavoriteModel(cocktailId, name, image, category, abv, categoryName, favorite)

        addToDispose(
            App.instanse?.dbFavorite?.favoriteDao()?.getCocktail(cocktailId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instanse?.dbFavorite?.favoriteDao()?.insert(favoriteModel)
                    } else {
                        App.instanse?.dbFavorite?.favoriteDao()?.setFavorite(favorite, cocktailId, category)
                    }
                })
    }
}
