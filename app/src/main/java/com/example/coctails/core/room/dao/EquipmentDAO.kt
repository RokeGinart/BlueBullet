package com.example.coctails.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.core.room.entity.glass_data.GlassFirebaseData
import io.reactivex.Single

@Dao
interface EquipmentDAO {

    @Query("SELECT * FROM firebase_equipment")
    fun getAllEquipment(): Single<List<EquipmentFirebaseData>>

    @Query("SELECT * FROM firebase_equipment WHERE id = :equipmentId")
    fun getEquipmentDetails(equipmentId : Int): Single<EquipmentFirebaseData>

    @Insert
    fun insert(equipment: EquipmentFirebaseData)

    @Delete
    fun delete(equipment: EquipmentFirebaseData)

    @Query("DELETE FROM firebase_equipment")
    fun deleteAllEquipment()
}