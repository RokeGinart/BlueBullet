package com.example.coctails.ui.screens.fragments.mainscreen

import com.example.coctails.ui.BasePresenter

abstract class MainScreenPresenter : BasePresenter<MainScreenView>() {

    abstract fun getCocktailsList(category : String)
    abstract fun getSearchCocktailsList(name : String)
    abstract fun getFirebaseData()

}