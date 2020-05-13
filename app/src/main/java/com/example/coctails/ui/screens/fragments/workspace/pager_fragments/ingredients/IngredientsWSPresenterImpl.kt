package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class IngredientsWSPresenterImpl : IngredientsWSPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("ingredients")

    override fun getIngredientList() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val allIngredientsList = mutableListOf<IngredientsModel>()

                dataSnapshot.children.forEach {
                    it.children.forEach { alc ->
                        allIngredientsList.add(alc.getValue(IngredientsModel::class.java)!!)
                    }
                }

                screenView?.showResult(allIngredientsList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}