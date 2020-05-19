package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import com.example.coctails.ui.BaseView
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection

interface IngredientsWSView : BaseView{
    fun showResult(ingredientList : List<IngredientModelSelection>, size : Int)
    fun showSortResult(showResult : List<IngredientModelSelection>)
    fun showListForSearch(searchResult : List<IngredientModelSelection>)
    fun successChanges()
}