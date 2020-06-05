package com.example.coctails.ui.screens.fragments.shopping

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ShoppingPresenterImpl : ShoppingPresenter() {

    override fun getAllShoppingItems() {
        addToDispose(
            App.instanse?.database?.shoppingDao()?.getAllShoppingItem()?.subscribeOn(Schedulers.io())?.observeOn(
                AndroidSchedulers.mainThread()
            )?.subscribe { t1, t2 ->
                if (t1 != null) {
                    if (t1.isNotEmpty()) {
                        screenView?.showResult(t1)
                    } else {
                        screenView?.showResult(t1)
                        screenView?.showMessage()
                    }
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
                    } else {
                        App.instanse?.database?.shoppingDao()?.delete(t1)
                    }
                })
    }
}