package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "category")
data class CategoryDB(
    @ColumnInfo(name = "category_c") var category: String = "",
    @ColumnInfo(name = "name_c") var name: String = ""
) : Serializable