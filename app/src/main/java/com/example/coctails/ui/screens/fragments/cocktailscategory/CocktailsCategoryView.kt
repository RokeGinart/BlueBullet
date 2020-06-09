package com.example.coctails.ui.screens.fragments.cocktailscategory

import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.BaseView

interface CocktailsCategoryView : BaseView {
    fun showCocktailsCategory(cocktailsList : List<CocktailFirebaseData>)
}