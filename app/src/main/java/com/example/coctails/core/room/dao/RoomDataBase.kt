package com.example.coctails.core.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.IngredientDBModel

@Database(entities = [FavoriteModel::class, IngredientDBModel::class],  version = 3)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDAO
    abstract fun ingredientDao() : IngredientsDAO
}