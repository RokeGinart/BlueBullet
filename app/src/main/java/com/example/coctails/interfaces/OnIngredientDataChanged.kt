package com.example.coctails.interfaces

interface OnIngredientDataChanged {
    fun dataIsChanged(isChanged : Boolean, ingredientId : Int, category: String)
}