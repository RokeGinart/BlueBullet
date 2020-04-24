package com.example.coctails.ui.screens.fragments.cocktaildetails

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CocktailDetailsPresenterImpl : CocktailDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")


    override fun getCocktailDetails(category: String, cocktailsId: String) {
        myRef.child(category).child(cocktailsId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    screenView?.showResult(dataSnapshot.getValue(Cocktails::class.java)!!)
                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
    }
}