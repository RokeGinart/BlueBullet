package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "info")
data class InfoDB(
    @ColumnInfo(name = "author") var author: String = "",
    @ColumnInfo(name = "country") var country: String = "",
    @ColumnInfo(name = "history") var history: String = "",
    @ColumnInfo(name = "year") var year: String = "",
    @Embedded var source: SourceDB? = null
) : Serializable
