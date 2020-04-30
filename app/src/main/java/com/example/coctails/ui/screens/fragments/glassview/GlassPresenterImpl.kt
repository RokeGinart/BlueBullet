package com.example.coctails.ui.screens.fragments.glassview

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GlassPresenterImpl : GlassPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("glass")

    override fun getGlass(glassId: Int) {
        myRef.child(glassId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                screenView?.showGlass(dataSnapshot.getValue(GlassDetails::class.java))
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}