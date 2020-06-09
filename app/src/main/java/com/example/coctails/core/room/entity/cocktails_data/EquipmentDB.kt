package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "equipment")
data class EquipmentDB (
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = ""
) : Serializable
