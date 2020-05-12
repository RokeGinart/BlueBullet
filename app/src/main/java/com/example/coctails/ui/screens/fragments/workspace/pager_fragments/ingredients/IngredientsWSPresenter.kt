package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import com.example.coctails.ui.BasePresenter

abstract class IngredientsWSPresenter : BasePresenter<IngredientsWSView>() {
    abstract fun getIngredientList()
}