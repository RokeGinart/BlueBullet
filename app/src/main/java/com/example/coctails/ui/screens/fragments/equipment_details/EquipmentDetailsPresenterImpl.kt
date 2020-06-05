package com.example.coctails.ui.screens.fragments.equipment_details

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.network.models.firebase.drink.Equipment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EquipmentDetailsPresenterImpl : EquipmentDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("equipment")

    override fun getEquipment(id: Int) {
        myRef.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                getItemDataFromDB(dataSnapshot.getValue(Equipment::class.java)!!)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun getItemDataFromDB(equipment: Equipment) {
        addToDispose(
            App.instanse?.database?.shoppingDao()?.getShoppingItem(
                equipment.id,
                "equipment"
            )?.subscribeOn(
                Schedulers.io()
            )
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showEquipment(equipment, true)
                    } else {
                        screenView?.showEquipment(equipment, false)
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