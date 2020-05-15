package com.example.coctails.ui.screens.fragments.ingredients_details

import com.example.coctails.core.App
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IngredientDetailsPresenterImpl : IngredientDetailsPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("ingredients")

    override fun getIngredientsData(category: String, ingredientId: Int) {
        myRef.child(category).child(ingredientId.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    screenView?.showIngredientResult(dataSnapshot.getValue(IngredientsModel::class.java)!!)
                }

                override fun onCancelled(p0: DatabaseError) {}
            })
    }

    override fun getIngredientFromDB(category: String, ingredientId: Int) {
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getIngredient(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        screenView?.showDatabaseResult(true)
                    } else {
                        screenView?.showDatabaseResult(false)
                    }
                })
    }

    override fun setIngredientToDB(category: String, ingredientId: Int) {
        val ingredientsDBModel = IngredientDBModel(ingredientId, category)
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getIngredient(ingredientId, category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    if (t1 != null) {
                        App.instanse?.database?.ingredientDao()?.delete(t1)
                    } else {
                        App.instanse?.database?.ingredientDao()?.insert(ingredientsDBModel)
                    }

                    screenView?.successChange()
                })
    }
}
