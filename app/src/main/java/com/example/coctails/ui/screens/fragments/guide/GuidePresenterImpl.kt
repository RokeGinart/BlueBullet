package com.example.coctails.ui.screens.fragments.guide

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GuidePresenterImpl : GuidePresenter() {

    override fun getAllGuide() {
        addToDispose(
            App.instance?.database?.guideFB()?.getAllGuides()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        if(t1.isNotEmpty())
                        screenView?.showAllGuide(t1)
                    }
                }
        )
    }
}