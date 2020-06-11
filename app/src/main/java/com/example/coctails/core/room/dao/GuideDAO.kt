package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.guide_data.GuideFirebaseData
import io.reactivex.Single

@Dao
interface GuideDAO {

    @Query("SELECT * FROM firebase_guide")
    fun getAllGuides(): Single<List<GuideFirebaseData>>

    @Query("SELECT * FROM firebase_guide WHERE id = :guideId")
    fun getGuidesDetails(guideId : Int): Single<GuideFirebaseData>

    @Insert
    fun insert(guide: GuideFirebaseData)

    @Delete
    fun delete(guide: GuideFirebaseData)

    @Query("DELETE FROM firebase_guide")
    fun deleteAllGuide()
}