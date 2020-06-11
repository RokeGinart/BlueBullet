package com.example.coctails.core.room.entity.guide_data

import androidx.room.*
import com.example.coctails.core.room.entity.TypeConvert
import java.io.Serializable

@Entity(tableName = "firebase_guide")
data class GuideFirebaseData(
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @Embedded var source: Source? = null,
    @TypeConverters(TypeConvert::class) var steps: List<Steps>? = null
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var idg: Long? = null
}
