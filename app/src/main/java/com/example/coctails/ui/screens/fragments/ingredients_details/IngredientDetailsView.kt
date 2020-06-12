package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.core.room.entity.ingredients_data.IngredientsFirebaseData
import com.example.coctails.ui.BaseView

interface IngredientDetailsView : BaseView {

    fun showIngredientResult(ingredientFirebaseData: IngredientsFirebaseData)
    fun showDatabaseResult(isSelected : Boolean, shoppingSelected : Boolean)
    fun successChange()
}