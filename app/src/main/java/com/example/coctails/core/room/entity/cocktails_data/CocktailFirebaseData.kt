package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.*
import com.example.coctails.core.room.entity.TypeConvert
import java.io.Serializable


@Entity(tableName = "firebase_cocktails")
data class CocktailFirebaseData(
    @ColumnInfo(name = "id")  var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "abv") var abv: Int = 0,
    @ColumnInfo(name = "cooktime") var cooktime: Int = 0,
    @ColumnInfo(name = "instruction") var instruction: String = "",
    @ColumnInfo(name = "mainIngredient") var mainIngredient: String = "",
    @Embedded var category: CategoryDB? = null,
    @Embedded var glass: GlassDB? = null,
    @Embedded var info : InfoDB? = null,
    @TypeConverters(TypeConvert::class) var equipment : List<EquipmentDB>? = null,
    @TypeConverters(TypeConvert::class) var ingredients: List<IngredientsDB>? = null

): Serializable {
    @PrimaryKey(autoGenerate = true)
    var idc: Long? = null
}

