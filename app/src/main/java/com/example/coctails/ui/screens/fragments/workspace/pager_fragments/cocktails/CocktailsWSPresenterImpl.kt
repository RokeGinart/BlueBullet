package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CocktailsWSPresenterImpl : CocktailsWSPresenter() {

    private val responseList = mutableListOf<CocktailFirebaseData>()

    override fun getAllCocktails() {
        responseList.clear()
        addToDispose(
            App.instance?.database?.cocktailFB()?.getAllFirebaseCocktails()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{t1, t2 ->
                    if(t1 != null){
                        responseList.addAll(t1)
                        showReadyCocktails()
                    }
                }
        )
    }

    override fun showReadyCocktails() {
        addToDispose(
            App.instance?.database?.ingredientDao()?.getAllIngredient()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.map { mapIng ->
                    val readyCocktails = mutableListOf<CocktailFirebaseData>()
                    responseList.forEach { cocktail ->
                        val cocktailsIngredientSize = cocktail.ingredients?.size
                        var sameIngredients = 0

                        cocktail.ingredients?.subList(1, cocktailsIngredientSize!!)?.forEach { ing ->

                            mapIng.forEach {
                                if (ing.category == it.category && ing.id == it.ingredientId) {
                                    sameIngredients +=1
                                }
                            }

                            if(sameIngredients == cocktailsIngredientSize.minus(1)){
                                readyCocktails.add(cocktail)
                            }
                        }
                    }
                    return@map readyCocktails
                }
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        if(t1.size > 0) {
                            screenView?.showResult(t1)
                        } else {
                            screenView?.showResult(t1)
                            screenView?.showMessage()
                        }
                    }
                }
        )
    }
}