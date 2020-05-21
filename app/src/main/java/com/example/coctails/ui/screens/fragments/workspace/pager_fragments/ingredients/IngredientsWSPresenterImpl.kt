package com.example.coctails.ui.screens.fragments.workspace.pager_fragments.ingredients

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

    private var selectedSort = 0

    private val allIngredientsList = mutableListOf<IngredientsModel>()
    private val tempIngredientList = mutableListOf<IngredientModelSelection>()
    private val ingredientsByCategoryList = mutableListOf<IngredientModelSelection>()

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
        tempIngredientList.clear()
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
                            it.abv,
                            it.image,
                            false
                        )
                        ingredientList.add(ingredientModelSelection)
                    }

                    t1?.forEach { dbIng ->
                        ingredientList.forEach { ing ->
                            if (dbIng.ingredientId == ing.ingredientId && dbIng.category == ing.category) {
                                ing.isSelected = true
                            }
                        }
                    }

                    tempIngredientList.addAll(ingredientList)

                    getSortItems(selectedSort, true)

                    screenView?.showResult(ingredientList, t1?.size!!)
                }
        )
    }

    override fun addCategorySort(categorySelected: Int) {
        when (categorySelected) {
            0 -> addCategory("alcohol")
            1 -> addCategory("liqueur")
            2 -> addCategory("fruits")
            3 -> addCategory("juice")
            4 -> addCategory("decoration")
            5 -> removeCategory("other")
        }
    }

    private fun addCategory(category: String) {
        tempIngredientList.forEach {
            if (it.category == category) {
                ingredientsByCategoryList.add(it)
            }
        }

        screenView?.showSortResult(ingredientsByCategoryList)
    }

    override fun removeCategorySort(categorySelected: Int) {
        when (categorySelected) {
            0 -> removeCategory("alcohol")
            1 -> removeCategory("liqueur")
            2 -> removeCategory("fruits")
            3 -> removeCategory("juice")
            4 -> removeCategory("decoration")
            5 -> removeCategory("other")
            6 -> {
                ingredientsByCategoryList.clear()
                checkListSize()
            }
        }
    }

    private fun removeCategory(category: String) {
        val removeItemsList = mutableListOf<IngredientModelSelection>()
        ingredientsByCategoryList.forEach { item ->
            if (item.category == category) {
                removeItemsList.add(item)
            }
        }

        ingredientsByCategoryList.removeAll(removeItemsList)
        checkListSize()
    }

    private fun checkListSize() {
        if (ingredientsByCategoryList.isNotEmpty()) {
            screenView?.showSortResult(ingredientsByCategoryList)
        } else {
            ingredientsByCategoryList.clear()
            getIngredientListFromDB()
        }
    }

    override fun getSortItems(sort: Int, change: Boolean) {
        selectedSort = sort
        val allItems = mutableListOf<IngredientModelSelection>()
        val local = tempIngredientList
        val selectedItems = mutableListOf<IngredientModelSelection>()
        val unselectedItems = mutableListOf<IngredientModelSelection>()

        if (ingredientsByCategoryList.isEmpty()) {
            allItems.addAll(tempIngredientList)
        } else {
            allItems.addAll(ingredientsByCategoryList)
        }

        if (change) {
            local.forEach { dbIng ->
                allItems.forEach { all ->
                    if (all.category == dbIng.category && all.ingredientId == dbIng.ingredientId && all.isSelected != dbIng.isSelected) {
                        allItems.remove(all)
                        allItems.add(dbIng)
                    }
                }
            }
        }

        allItems.forEach {
            if (it.isSelected) {
                selectedItems.add(it)
            } else {
                unselectedItems.add(it)
            }
        }

        when (sort) {
            0 -> screenView?.showSortResult(allItems)
            1 -> screenView?.showSortResult(selectedItems)
            2 -> screenView?.showSortResult(unselectedItems)
        }
    }

    override fun getListForSearch() {
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
                            it.abv,
                            it.image,
                            false
                        )
                        ingredientList.add(ingredientModelSelection)
                    }

                    t1?.forEach { dbIng ->
                        ingredientList.forEach { ing ->
                            if (dbIng.ingredientId == ing.ingredientId && dbIng.category == ing.category) {
                                ing.isSelected = true
                            }
                        }
                    }

                    screenView?.showListForSearch(ingredientList)
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