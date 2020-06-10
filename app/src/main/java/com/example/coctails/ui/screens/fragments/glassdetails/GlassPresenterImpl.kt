package com.example.coctails.ui.screens.fragments.glassdetails

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GlassPresenterImpl : GlassPresenter() {

    override fun getGlass(glassId: Int) {
        addToDispose(
            App.instanse?.database?.glassFB()?.getGlassDetails(glassId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        getItemDataFromDB(t1)
                    }
                }
        )
    }

    private fun getItemDataFromDB(glass: GlassFirebaseData) {
        addToDispose(
            App.instanse?.database?.shoppingDao()?.getShoppingItem(
                glass.id,
                "glass"
            )?.subscribeOn(
                Schedulers.io()
            )
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showGlass(glass, true)
                    } else {
                        screenView?.showGlass(glass, false)
                    }
                }
        )
    }

    override fun updateShoppingStatus(
        itemId: Int,
        name: String,
        image: String,
        mainCategory: String,
        category: String
    ) {
        val shoppingItem = Shopping(itemId, name, image, mainCategory, category, true)

        addToDispose(
            App.instanse?.database?.shoppingDao()?.getShoppingItem(itemId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instanse?.database?.shoppingDao()?.insert(shoppingItem)
                        screenView?.changesSuccess(itemId, true)
                    } else {
                        App.instanse?.database?.shoppingDao()?.delete(t1)
                        screenView?.changesSuccess(itemId, false)
                    }
                })
    }
}