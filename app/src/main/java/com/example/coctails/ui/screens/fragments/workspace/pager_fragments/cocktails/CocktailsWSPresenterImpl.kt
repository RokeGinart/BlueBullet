package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.cocktails

import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.network.models.firebase.drink.Cocktails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CocktailsWSPresenterImpl : CocktailsWSPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("drink")

    private val responseList = mutableListOf<Cocktails>()

    override fun getAllCocktails() {
        responseList.clear()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {
                    it.children.forEach { cocktails ->
                        responseList.add(cocktails.getValue(Cocktails::class.java)!!)
                    }
                }

                showReadyCocktails()

            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    override fun showReadyCocktails() {
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getAllIngredient()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.map { mapIng ->
                    val readyCocktails = mutableListOf<Cocktails>()
                    responseList.forEach { cocktail ->
                        val cocktailsIngredientSize = cocktail.ingredients?.size
                        var sameIngredients = 0

                        cocktail.ingredients?.subList(1, cocktailsIngredientSize!!)?.forEach { ing ->

                            mapIng.forEach {
                                if (ing.category == it.category && ing.id == it.ingredientId) {
                                    sameIngredients +=1
                                }
                            }

                            if(sameIngredients == cocktailsIngredientSize.minus(1)){
                                readyCocktails.add(cocktail)
                            }
                        }
                    }
                    return@map readyCocktails
                }
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        if(t1.size > 0) {
                            screenView?.showResult(t1)
                        } else {
                            screenView?.showResult(t1)
                            screenView?.showMessage()
                        }
                    }
                }
        )
    }
}