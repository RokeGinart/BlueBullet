package com.example.coctails.ui.screens.fragments.glass

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GlassWSPresenterImpl : GlassWSPresenter() {
    override fun getGlassList() {
        addToDispose(
            App.instanse?.database?.glassFB()?.getAllGlass()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showGlassList(t1)
                    }
                }
        )
    }
}