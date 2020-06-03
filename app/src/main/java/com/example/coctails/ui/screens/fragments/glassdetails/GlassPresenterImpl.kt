package com.example.coctails.ui.screens.fragments.glassdetails

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.network.models.firebase.drink.Equipment
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GlassPresenterImpl : GlassPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("glass")

    override fun getGlass(glassId: Int) {
        myRef.child(glassId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                getItemDataFromDB(dataSnapshot.getValue(GlassDetails::class.java)!!)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun getItemDataFromDB(glass: GlassDetails) {
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
                    } else {
                        App.instanse?.database?.shoppingDao()?.delete(t1)
                    }
                })
    }
}