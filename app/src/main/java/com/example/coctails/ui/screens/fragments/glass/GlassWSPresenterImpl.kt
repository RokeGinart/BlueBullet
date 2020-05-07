package com.example.coctails.ui.screens.fragments.glass

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.GlassDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GlassWSPresenterImpl : GlassWSPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("glass")

    override fun getGlassList() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<GlassDetails>()

                dataSnapshot.children.forEach {
                    responseList.add(it.getValue(GlassDetails::class.java)!!)
                }

                screenView?.showGlassList(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}