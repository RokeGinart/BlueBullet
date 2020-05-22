package com.example.coctails.ui.screens.fragments.equipment

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Equipment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EquipmentPresenterImpl : EquipmentPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("equipment")

    override fun getAllEquipment() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<Equipment>()

                dataSnapshot.children.forEach {
                    responseList.add(it.getValue(Equipment::class.java)!!)
                }

                screenView?.showEquipment(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}