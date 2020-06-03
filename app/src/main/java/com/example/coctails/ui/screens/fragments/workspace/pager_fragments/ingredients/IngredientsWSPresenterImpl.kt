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
            0 -> addCategory()
            1 -> addTwoCategories(1)
            2 -> addTwoCategories(2)
            3 -> addTwoCategories(3)
            4 -> addTwoCategories(4)
            5 -> addOtherCategory()
        }
    }

    private fun addCategory() {
        tempIngredientList.forEach {
            if (it.category == "alcohol") {
                ingredientsByCategoryList.add(it)
            }
        }

        screenView?.showSortResult(ingredientsByCategoryList)
    }

    private fun addOtherCategory() {
        tempIngredientList.forEach {
            if (it.category == "ice" || it.category == "coffee" || it.category == "other" || it.category == "sauce") {
                ingredientsByCategoryList.add(it)
            }
        }

        screenView?.showSortResult(ingredientsByCategoryList)
    }

    private fun addTwoCategories(select : Int) {
        var firstCategory = ""
        var secondCategory = ""

        when (select) {
            1 -> {
                firstCategory = "liqueur"
                secondCategory = "syrup"
            }
            2 -> {
                firstCategory = "fruits"
                secondCategory = "vegetable"
            }
            3 -> {
                firstCategory = "juice"
                secondCategory = "soda"
            }
            4 -> {
                firstCategory = "decoration"
                secondCategory = "flavor"
            }
        }

        tempIngredientList.forEach {
            if (it.category == firstCategory || it.category == secondCategory) {
                ingredientsByCategoryList.add(it)
            }
        }

        screenView?.showSortResult(ingredientsByCategoryList)
    }

    override fun removeCategorySort(categorySelected: Int) {
        when (categorySelected) {
            0 -> removeCategory()
            1 -> removeTwoCategory(1)
            2 -> removeTwoCategory(2)
            3 -> removeTwoCategory(3)
            4 -> removeTwoCategory(4)
            5 -> removeOtherCategory()
            6 -> {
                ingredientsByCategoryList.clear()
                checkListSize()
            }
        }
    }

    private fun removeOtherCategory() {
        val removeItemsList = mutableListOf<IngredientModelSelection>()
        ingredientsByCategoryList.forEach { item ->
            if (item.category == "ice" || item.category == "coffee" || item.category == "other" || item.category == "sauce") {
                removeItemsList.add(item)
            }
        }

        ingredientsByCategoryList.removeAll(removeItemsList)
        checkListSize()
    }

    private fun removeCategory() {
        val removeItemsList = mutableListOf<IngredientModelSelection>()
        ingredientsByCategoryList.forEach { item ->
            if (item.category == "alcohol") {
                removeItemsList.add(item)
            }
        }

        ingredientsByCategoryList.removeAll(removeItemsList)
        checkListSize()
    }

    private fun removeTwoCategory(select: Int) {
        var firstCategory = ""
        var secondCategory = ""

        when (select) {
            1 -> {
                firstCategory = "liqueur"
                secondCategory = "syrup"
            }
            2 -> {
                firstCategory = "fruits"
                secondCategory = "vegetable"
            }
            3 -> {
                firstCategory = "juice"
                secondCategory = "soda"
            }
            4 -> {
                firstCategory = "decoration"
                secondCategory = "flavor"
            }
        }

        val removeItemsList = mutableListOf<IngredientModelSelection>()
        ingredientsByCategoryList.forEach { item ->
            if (item.category == firstCategory || item.category == secondCategory) {
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
        val selectedItems = mutableListOf<IngredientModelSelection>()
        val unselectedItems = mutableListOf<IngredientModelSelection>()

        if (ingredientsByCategoryList.isEmpty()) {
            allItems.addAll(tempIngredientList)
        } else {
            allItems.addAll(ingredientsByCategoryList)
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