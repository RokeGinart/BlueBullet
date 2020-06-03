package com.example.coctails.ui.screens.fragments.shopping

import com.example.coctails.ui.BasePresenter

abstract class ShoppingPresenter : BasePresenter<ShoppingView>() {

    abstract fun getAllShoppingItems()
    abstract fun updateShoppingStatus(itemId : Int, name : String, image : String, mainCategory: String, category : String)
}