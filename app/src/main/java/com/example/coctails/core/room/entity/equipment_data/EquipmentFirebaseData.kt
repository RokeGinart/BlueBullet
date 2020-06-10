package com.example.coctails.core.room.entity.equipment_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_equipment")
data class EquipmentFirebaseData(
   @ColumnInfo(name = "id") var id: Int = 0,
   @ColumnInfo(name = "guide") var guide : Int = 0,
   @ColumnInfo(name = "name") var name: String = "",
   @ColumnInfo(name = "image") var image: String = "",
   @ColumnInfo(name = "link") var link: String = "",
   @ColumnInfo(name = "description") var description: String = ""
){
    @PrimaryKey(autoGenerate = true)
    var ide: Long? = null
}
