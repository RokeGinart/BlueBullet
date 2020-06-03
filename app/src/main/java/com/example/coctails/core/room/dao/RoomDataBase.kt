package com.example.coctails.core.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.core.room.entity.Shopping

@Database(entities = [FavoriteModel::class, IngredientDBModel::class, Shopping::class],  version = 5)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDAO
    abstract fun ingredientDao() : IngredientsDAO
    abstract fun shoppingDao() : ShoppingDAO
}