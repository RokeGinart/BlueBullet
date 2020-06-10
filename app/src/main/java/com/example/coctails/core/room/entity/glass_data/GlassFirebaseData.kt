package com.example.coctails.core.room.entity.glass_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_glass")
data class GlassFirebaseData(
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "link") var link: String = "",
    @ColumnInfo(name = "description") var description: String = "")
{
    @PrimaryKey(autoGenerate = true)
    var idg: Long? = null
}