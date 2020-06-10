package com.example.coctails.ui.screens.fragments.favorites

import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.ui.BaseView

interface FavoriteView : BaseView {

    fun showFavoriteList(favoriteModel: List<FavoriteModel>)
    fun showMessage()
    fun getCocktail(cocktails: CocktailFirebaseData)
}