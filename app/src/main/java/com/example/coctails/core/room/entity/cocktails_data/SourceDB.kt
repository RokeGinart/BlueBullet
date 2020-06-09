package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "source")
data class SourceDB(
    @ColumnInfo(name = "link_s") var link: String? = "",
    @ColumnInfo(name = "name_s") var name: String = ""
) : Serializable