package com.example.coctails.ui.screens.fragments.favorites.interfaces

import java.io.Serializable

interface OnFavoriteChange : Serializable{

    fun favoriteStatusChange(category : String, id : Int, isSelected : Boolean)
}