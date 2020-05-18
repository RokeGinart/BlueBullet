package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.FavoriteModel
import io.reactivex.Single

@Dao
interface FavoriteDAO {

    @Query("SELECT * FROM favorite_model")
    fun getAllFavorite(): Single<List<FavoriteModel>>

    @Query("SELECT * FROM favorite_model WHERE cocktail_id = :cocktailId AND category = :category")
    fun getCocktail(cocktailId : Int, category : String): Single<FavoriteModel>

    @Insert
    fun insert(favoriteList: FavoriteModel)

    @Delete
    fun delete(favoriteList: FavoriteModel)
}