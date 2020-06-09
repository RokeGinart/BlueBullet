package com.example.coctails.core.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.core.room.entity.cocktails_data.TypeConvert

@Database(entities = [FavoriteModel::class, IngredientDBModel::class, Shopping::class, CocktailFirebaseData::class],  version = 12)
@TypeConverters(TypeConvert::class)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun cocktailFB() : CocktailsFirebaseDataDAO

    abstract fun favoriteDao() : FavoriteDAO
    abstract fun ingredientDao() : IngredientsDAO
    abstract fun shoppingDao() : ShoppingDAO
}