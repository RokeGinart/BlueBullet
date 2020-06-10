package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import io.reactivex.Single

@Dao
interface GlassDAO {

    @Query("SELECT * FROM firebase_glass")
    fun getAllGlass(): Single<List<GlassFirebaseData>>

    @Query("SELECT * FROM firebase_glass WHERE id = :glassId")
    fun getGlassDetails(glassId : Int): Single<GlassFirebaseData>

    @Insert
    fun insert(glass: GlassFirebaseData)

    @Delete
    fun delete(glass: GlassFirebaseData)

    @Query("DELETE FROM firebase_glass")
    fun deleteAllGlass()
}