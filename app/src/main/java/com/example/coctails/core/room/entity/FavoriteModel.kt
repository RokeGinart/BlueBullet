package com.example.coctails.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_model")
data class FavoriteModel(

    @ColumnInfo(name = "cocktail_id") var cocktailId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "abv") var abv: Int,
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}