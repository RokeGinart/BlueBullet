package com.example.coctails.ui.screens.fragments.equipment

import com.example.coctails.core.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EquipmentPresenterImpl : EquipmentPresenter() {

    override fun getAllEquipment() {
        addToDispose(
            App.instance?.database?.equipmentFB()?.getAllEquipment()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showEquipment(t1)
                    }
                }
        )
    }
}