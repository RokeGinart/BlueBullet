package com.example.coctails.ui.screens.fragments.guide_detail

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Guide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GuideDetailsPresenterImpl : GuideDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("guides")

    override fun getGuide(id: Int) {
        myRef.child(id.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                screenView?.showGuide(dataSnapshot.getValue(Guide::class.java))
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}