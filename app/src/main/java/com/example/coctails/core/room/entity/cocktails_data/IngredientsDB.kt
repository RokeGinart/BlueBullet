package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "ingredientsDB")
data class IngredientsDB(
    @ColumnInfo(name = "id_i") var id: Int = 0,
    @ColumnInfo(name = "name_i") var name: String = "",
    @ColumnInfo(name = "category_i") var category: String = ""
) : Serializable