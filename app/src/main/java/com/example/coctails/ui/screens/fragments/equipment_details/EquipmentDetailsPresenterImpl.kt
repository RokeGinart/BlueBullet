package com.example.coctails.ui.screens.fragments.equipment_details

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Equipment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EquipmentDetailsPresenterImpl : EquipmentDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("equipment")

    override fun getEquipment(id: Int) {
        myRef.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                screenView?.showEquipment(dataSnapshot.getValue(Equipment::class.java))
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}