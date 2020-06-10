package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import io.reactivex.Single

@Dao
interface CocktailsFirebaseDataDAO {

    @Query("SELECT * FROM firebase_cocktails")
    fun getAllFirebaseCocktails(): Single<List<CocktailFirebaseData>>

    @Insert
    fun insert(cocktailFB: CocktailFirebaseData)

    @Delete
    fun delete(cocktailFB: CocktailFirebaseData)

    @Query("DELETE FROM firebase_cocktails")
    fun deleteAllData()
}