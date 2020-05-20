package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import com.example.coctails.ui.BasePresenter

abstract class IngredientsWSPresenter : BasePresenter<IngredientsWSView>() {
    abstract fun getIngredientList()
    abstract fun getIngredientListFromDB()
    abstract fun setIngredientToDB(ingredientId: Int, category: String)
    abstract fun getSortItems(sort : Int)
    abstract fun addCategorySort(categorySelected : Int)
    abstract fun removeCategorySort(categorySelected : Int)
    abstract fun getListForSearch()
}