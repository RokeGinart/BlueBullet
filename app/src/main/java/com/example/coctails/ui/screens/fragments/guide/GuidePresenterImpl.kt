package com.example.coctails.ui.screens.fragments.guide

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Guide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GuidePresenterImpl : GuidePresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("guides")

    override fun getAllGuide() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<Guide>()

                dataSnapshot.children.forEach {
                    responseList.add(it.getValue(Guide::class.java)!!)
                }

                screenView?.showAllGuide(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}