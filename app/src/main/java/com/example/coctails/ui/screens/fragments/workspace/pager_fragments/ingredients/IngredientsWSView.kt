package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.BaseView

interface IngredientsWSView : BaseView{
    fun showResult(ingredientList : List<IngredientsModel>)
}