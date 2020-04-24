package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CocktailDetailsPresenterImpl : CocktailDetailsPresenter() {

    override fun getCocktailDetails(cocktailsId: String) {
            addToDispose(
                App.instanse?.api?.getCocktailById(cocktailsId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe { t1, t2 -> screenView?.showResult(t1)})
        }
}