package com.example.coctails.ui.screens.fragments.cocktailscategory

import androidx.annotation.NonNull
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CocktailsCategoryPresenterImpl : CocktailsCategoryPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")

    override fun getCocktailsByCategory(category: String) {
        myRef.child(category).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<Cocktails>()

                dataSnapshot.children.forEach {
                    responseList.add(it.getValue(Cocktails::class.java)!!)
                }

                screenView?.showCocktailsCategory(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    override fun getAllCocktails() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val responseList = mutableListOf<Cocktails>()

                dataSnapshot.children.forEach {
                    it.children.forEach { cocktails ->
                        responseList.add(cocktails.getValue(Cocktails::class.java)!!)
                    }
                }

                screenView?.showCocktailsCategory(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    override fun getCocktailsByIngredient(ingredient: String) {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val allCocktails = mutableListOf<Cocktails>()
                val responseList = mutableListOf<Cocktails>()

                dataSnapshot.children.forEach {
                    it.children.forEach { cocktails ->
                        allCocktails.add(cocktails.getValue(Cocktails::class.java)!!)
                    }
                }

                allCocktails.forEach{
                    if(it.mainIngredient == ingredient){
                        responseList.add(it)
                    }
                }

                screenView?.showCocktailsCategory(responseList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}

