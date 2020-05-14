package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.BaseView

interface IngredientDetailsView : BaseView {

    fun showIngredientResult(ingredientModel: IngredientsModel)
    fun showDatabaseResult(isSelected : Boolean)
}