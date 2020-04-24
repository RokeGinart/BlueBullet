package com.example.coctails.ui.screens.fragments.mainscreen

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainScreenPresenterImpl : MainScreenPresenter() {

    override fun getCocktailsList(category: String) {
        addToDispose(
            App.instanse?.api?.getCocktailsByCategory(category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 -> screenView?.showAllCocktails(t1.drinks)})
    }

    override fun getSearchCocktailsList(name: String) {
        addToDispose(
            App.instanse?.api?.searchCocktails(name)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    screenView?.showSearchResult(t1?.drinks)
                })
    }
}