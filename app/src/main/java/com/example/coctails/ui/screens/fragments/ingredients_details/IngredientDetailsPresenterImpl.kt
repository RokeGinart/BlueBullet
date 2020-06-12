package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.core.room.entity.Shopping
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IngredientDetailsPresenterImpl : IngredientDetailsPresenter() {

    override fun getIngredientsData(category: String, ingredientId: Int) {

        addToDispose(
            App.instance?.database?.ingredientsFB()?.getIngredientsDetails(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showIngredientResult(t1)
                    }
                })
    }

    override fun getIngredientFromDB(category: String, ingredientId: Int) {
        addToDispose(
            App.instance?.database?.ingredientDao()?.getIngredient(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        getShopDB(category, ingredientId, true)
                    } else {
                        getShopDB(category, ingredientId, false)
                    }
                })
    }

    private fun getShopDB(category: String, ingredientId: Int, selected: Boolean) {
        addToDispose(
            App.instance?.database?.shoppingDao()?.getShoppingItem(
                ingredientId,
                category
            )?.subscribeOn(
                Schedulers.io()
            )
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showDatabaseResult(selected, true)
                    } else {
                        screenView?.showDatabaseResult(selected, false)
                    }
                }
        )
    }

    override fun setIngredientToDB(category: String, ingredientId: Int) {
        val ingredientsDBModel = IngredientDBModel(ingredientId, category)
        addToDispose(
            App.instance?.database?.ingredientDao()?.getIngredient(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        App.instance?.database?.ingredientDao()?.delete(t1)
                    } else {
                        App.instance?.database?.ingredientDao()?.insert(ingredientsDBModel)
                    }

                    screenView?.successChange()
                })
    }

    override fun updateShoppingStatus(
        itemId: Int,
        name: String,
        image: String,
        mainCategory: String,
        category: String
    ) {
        val shoppingItem = Shopping(itemId, name, image, mainCategory, category, true)

        addToDispose(
            App.instance?.database?.shoppingDao()?.getShoppingItem(itemId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instance?.database?.shoppingDao()?.insert(shoppingItem)
                    } else {
                        App.instance?.database?.shoppingDao()?.delete(t1)
                    }

                    screenView?.successChange()
                })
    }
}
