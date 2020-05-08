package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.ui.BasePresenter

abstract class IngredientDetailsPresenter : BasePresenter<IngredientDetailsView>() {

    abstract fun getIngredientsData(category: String, ingredientId : Int)
}