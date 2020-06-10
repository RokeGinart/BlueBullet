package com.example.coctails.core.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coctails.core.room.entity.FavoriteModel
import com.example.coctails.core.room.entity.IngredientDBModel
import com.example.coctails.core.room.entity.Shopping
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.core.room.entity.cocktails_data.TypeConvert
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData

@Database(entities = [FavoriteModel::class,
    IngredientDBModel::class,
    Shopping::class,
    CocktailFirebaseData::class,
    GlassFirebaseData::class,
    EquipmentFirebaseData::class],  version = 14)
@TypeConverters(TypeConvert::class)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun cocktailFB() : CocktailsFirebaseDataDAO
    abstract fun glassFB() : GlassDAO
    abstract fun equipmentFB() : EquipmentDAO

    abstract fun favoriteDao() : FavoriteDAO
    abstract fun ingredientDao() : IngredientsDAO
    abstract fun shoppingDao() : ShoppingDAO
}