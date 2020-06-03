package com.example.coctails.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_data")
data class Shopping(
    @ColumnInfo(name = "item_id") var itemId: Int,
    @ColumnInfo(name = "item_name") var name: String,
    @ColumnInfo(name = "item_image") var image: String,
    @ColumnInfo(name = "main_category") var mainCategory: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "selected") var selected: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}