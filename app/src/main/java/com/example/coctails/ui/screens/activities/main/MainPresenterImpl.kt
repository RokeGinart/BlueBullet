package com.example.coctails.ui.screens.activities.main

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenterImpl : MainPresenter(){

    override fun getCocktail(name: String) {
        addToDispose(
            App.instanse?.api?.searchCocktails(name)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 -> screenView?.showCocktails(t1)})
    }
}