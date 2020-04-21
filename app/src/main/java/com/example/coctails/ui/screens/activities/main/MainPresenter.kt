package com.example.coctails.ui.screens.activities.main

import com.example.coctails.ui.BasePresenter

abstract class MainPresenter : BasePresenter<MainView>() {

    abstract fun getCocktail(name : String)
}
