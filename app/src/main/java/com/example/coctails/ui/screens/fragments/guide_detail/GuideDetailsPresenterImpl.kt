package com.example.coctails.ui.screens.fragments.guide_detail

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GuideDetailsPresenterImpl : GuideDetailsPresenter() {

    override fun getGuide(id: Int) {
        addToDispose(
            App.instanse?.database?.guideFB()?.getGuidesDetails(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showGuide(t1)
                    }
                }
        )
    }
}