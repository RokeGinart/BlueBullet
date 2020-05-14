package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.ui.BaseView
import com.example.coctails.ui.screens.fragments.cocktaildetails.model.IngredientModelCD

interface CocktailDetailsView : BaseView {
    fun showFavorite(inFavorite: Boolean)
    fun showIngredientResult(ingredientsList: List<IngredientModelCD>)
}