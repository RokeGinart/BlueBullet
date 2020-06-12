package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.ingredients_data.IngredientsFirebaseData
import io.reactivex.Single

@Dao
interface IngredientsFirebaseDAO {

    @Query("SELECT * FROM firebase_ingredients")
    fun getAllFirebaseIngredients(): Single<List<IngredientsFirebaseData>>

    @Query("SELECT * FROM firebase_ingredients WHERE id = :ingredientID AND category_i = :category")
    fun getIngredientsDetails(ingredientID : Int, category : String): Single<IngredientsFirebaseData>

    @Insert
    fun insert(ingredient: IngredientsFirebaseData)

    @Delete
    fun delete(ingredient: IngredientsFirebaseData)

    @Query("DELETE FROM firebase_ingredients")
    fun deleteAllFirebaseIngredients()
}