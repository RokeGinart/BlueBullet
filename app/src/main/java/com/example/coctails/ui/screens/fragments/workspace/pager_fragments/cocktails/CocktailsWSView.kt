package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.ui.BaseView

interface CocktailsWSView : BaseView {

    fun showResult(cocktails : List<CocktailFirebaseData>)
    fun showMessage()
}