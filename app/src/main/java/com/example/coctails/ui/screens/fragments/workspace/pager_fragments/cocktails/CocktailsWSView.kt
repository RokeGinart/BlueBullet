package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.BaseView

interface CocktailsWSView : BaseView {

    fun showResult(cocktails : List<Cocktails>)
    fun showMessage()
}