package com.example.coctails.core.room.entity.ingredients_data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "category")
data class CategoryDB(
    @ColumnInfo(name = "category_i") var category: String = "",
    @ColumnInfo(name = "name_i") var name: String = ""
)