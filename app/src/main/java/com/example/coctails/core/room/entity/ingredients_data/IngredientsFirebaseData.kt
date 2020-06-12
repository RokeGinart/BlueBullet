package com.example.coctails.core.room.entity.ingredients_data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_ingredients")
data class IngredientsFirebaseData(
    @ColumnInfo(name = "id") var id : Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "country") var country: String = "",
    @ColumnInfo(name = "link") var link: String = "",
    @ColumnInfo(name = "abv") var abv: Int = 0,
    @Embedded var category: CategoryDB? = null
){
    @PrimaryKey(autoGenerate = true)
    var idi: Long? = null
}
