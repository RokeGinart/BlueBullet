package com.example.coctails.core.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coctails.core.room.entity.FavoriteModel

@Database(entities = [FavoriteModel::class], version = 1)
abstract class RoomFavoriteDB : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDAO
}