package com.example.coctails.core.room.entity.cocktails_data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class TypeConvert {

    var gson = Gson()

    @TypeConverter
    fun ingredientsToList(data: String?): List<IngredientsDB?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<IngredientsDB?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun igredientsToString(someObjects: List<IngredientsDB?>?): String? {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun equipmentToList(data: String?): List<EquipmentDB?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<EquipmentDB?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun equipmentToString(someObjects: List<EquipmentDB?>?): String? {
        return gson.toJson(someObjects)
    }
}