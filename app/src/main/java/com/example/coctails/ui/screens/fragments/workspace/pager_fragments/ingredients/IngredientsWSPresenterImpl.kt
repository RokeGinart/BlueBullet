package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

import android.util.Log
import androidx.annotation.NonNull
import com.example.coctails.core.App
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.network.models.firebase.drink.IngredientsModel
import com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients.model.IngredientModelSelection
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class IngredientsWSPresenterImpl : IngredientsWSPresenter() {

    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("ingredients")

    val allIngredientsList = mutableListOf<IngredientsModel>()

    override fun getIngredientList() {
        allIngredientsList.clear()
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    it.children.forEach { alc ->
                        allIngredientsList.add(alc.getValue(IngredientsModel::class.java)!!)
                    }
                }

                getIngredientListFromDB()
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    override fun getIngredientListFromDB() {
        addToDispose(
            App.instanse?.database?.ingredientDao()?.getAllIngredient()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { t1, t2 ->
                    val ingredientList = mutableListOf<IngredientModelSelection>()

                    allIngredientsList.forEach {
                        val ingredientModelSelection = IngredientModelSelection(
                            it.id,
                            it.category?.category!!,
                            it.name,
                            it.image,
                            false
                        )
                        ingredientList.add(ingredientModelSelection)
                    }

                    t1?.forEach{ dbIng ->
                        ingredientList.forEach{ing ->
                            if(dbIng.ingredientId == ing.ingredientId && dbIng.category == ing.category){
                                ing.isSelected = true
                            }
                        }
                    }

                    screenView?.showResult(ingredientList, t1?.size!!)
                }
        )
    }

    override fun setIngredientToDB(ingredientId: Int, category: String) {
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

                    screenView?.successChanges()
                })
    }
}