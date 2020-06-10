package com.example.coctails.ui.screens.fragments.cocktailscategory

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CocktailsCategoryPresenterImpl : CocktailsCategoryPresenter() {

    override fun getCocktailsByCategory(category: String) {
        addToDispose(
            App.instanse?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{t1, t2 ->
                    val responseList = mutableListOf<CocktailFirebaseData>()

                    t1?.forEach{
                        if(it.category?.category == category){
                            responseList.add(it)
                        }
                    }

                    screenView?.showCocktailsCategory(responseList)
                }
        )
    }

    override fun getAllCocktails() {
        addToDispose(
            App.instanse?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{t1, t2 ->
                    screenView?.showCocktailsCategory(t1)
                }
        )
    }

    override fun getCocktailsByIngredient(ingredient: String) {
        addToDispose(
            App.instanse?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{t1, t2 ->
                    val responseList = mutableListOf<CocktailFirebaseData>()

                    t1?.forEach{
                        if(it.mainIngredient == ingredient){
                            responseList.add(it)
                        }
                    }

                    screenView?.showCocktailsCategory(responseList)
                }
        )
    }
}

