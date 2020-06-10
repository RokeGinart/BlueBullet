package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CocktailDetailsPresenterImpl : CocktailDetailsPresenter() {

    override fun getFavorite(cocktailId: Int, category: String) {
        addToDispose(
            App.instanse?.database?.favoriteDao()?.getCocktail(cocktailId, category)
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
            App.instanse?.database?.favoriteDao()?.getCocktail(cocktailId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instanse?.database?.favoriteDao()?.insert(favoriteModel)
                    } else {
                        App.instanse?.database?.favoriteDao()?.delete(t1)
                    }

                    screenView?.success()
                })
    }

    override fun getIngredientDetail(cocktails: CocktailFirebaseData) {
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getAllIngredient()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    val ingredientList = mutableListOf<IngredientModelCD>()

                    cocktails.ingredients?.subList(1, cocktails.ingredients?.size!!)?.forEach {
                        val ingredientModelCD = IngredientModelCD(it.id, it.category, it.name, false)
                        ingredientList.add(ingredientModelCD)
                    }

                    t1?.forEach{ dbIng ->
                        ingredientList.forEach{ing ->
                            if(dbIng.ingredientId == ing.ingredientId && dbIng.category == ing.category){
                                ing.isSelected = true
                            }
                        }
                    }

                    screenView?.showIngredientResult(ingredientList)
                }
        )
    }

    override fun setIngredientToDB(ingredientId: Int, category: String) {
        val ingredientsDBModel = IngredientDBModel(ingredientId, category)
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getIngredient(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        App.instanse?.database?.ingredientDao()?.delete(t1)
                    } else {
                        App.instanse?.database?.ingredientDao()?.insert(ingredientsDBModel)
                    }

                    screenView?.success()
                })
    }
}
