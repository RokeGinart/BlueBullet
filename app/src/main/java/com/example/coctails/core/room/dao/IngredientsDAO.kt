package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.IngredientDBModel
import io.reactivex.Single

@Dao
interface IngredientsDAO {

    @Query("SELECT * FROM ingredients_model")
    fun getAllIngredient(): Single<List<IngredientDBModel>>

    @Query("SELECT * FROM ingredients_model WHERE ingredients_id = :ingredientId AND category = :category")
    fun getIngredient(ingredientId : Int, category : String): Single<IngredientDBModel>

    @Insert
    fun insert(ingredient: IngredientDBModel)

    @Delete
    fun delete(ingredient: IngredientDBModel)
}