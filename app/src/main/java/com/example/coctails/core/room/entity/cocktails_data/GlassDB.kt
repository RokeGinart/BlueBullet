package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "glass")
data class GlassDB(
    @ColumnInfo(name = "id_g") var id: Int = 0,
    @ColumnInfo(name = "name_g") var name: String = ""
) : Serializable