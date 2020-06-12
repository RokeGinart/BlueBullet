package com.example.coctails.ui.screens.fragments.equipment_details

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EquipmentDetailsPresenterImpl : EquipmentDetailsPresenter() {

    override fun getEquipment(id: Int) {
        addToDispose(
            App.instance?.database?.equipmentFB()?.getEquipmentDetails(id)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        getItemDataFromDB(t1)
                    }
                }
        )
    }

    private fun getItemDataFromDB(equipmentFirebaseData: EquipmentFirebaseData) {
        addToDispose(
            App.instance?.database?.shoppingDao()?.getShoppingItem(
                equipmentFirebaseData.id,
                "equipment"
            )?.subscribeOn(
                Schedulers.io()
            )
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showEquipment(equipmentFirebaseData, true)
                    } else {
                        screenView?.showEquipment(equipmentFirebaseData, false)
                    }
                }
        )
    }

    override fun updateShoppingStatus(
        itemId: Int,
        name: String,
        image : String,
        mainCategory: String,
        category: String
    ) {
        val shoppingItem = Shopping(itemId, name, image, mainCategory, category, true)

        addToDispose(
            App.instance?.database?.shoppingDao()?.getShoppingItem(itemId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 == null) {
                        App.instance?.database?.shoppingDao()?.insert(shoppingItem)
                        screenView?.changesSuccess(itemId, true)
                    } else {
                        App.instance?.database?.shoppingDao()?.delete(t1)
                        screenView?.changesSuccess(itemId, false)
                    }
                })
    }
}