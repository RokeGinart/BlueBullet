package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IngredientDetailsPresenterImpl : IngredientDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("ingredients")

    override fun getIngredientsData(category: String, ingredientId: Int) {
        myRef.child(category).child(ingredientId.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                screenView?.showIngredientResult(dataSnapshot.getValue(IngredientsModel::class.java)!!)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}