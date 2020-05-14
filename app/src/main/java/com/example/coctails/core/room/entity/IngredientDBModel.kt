package com.example.coctails.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients_model")
data class IngredientDBModel (

    @ColumnInfo(name = "ingredients_id") var ingredientId: Int,
    @ColumnInfo(name = "category") var category: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}