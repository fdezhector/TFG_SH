package com.example.tfg_sh.bbdd

import androidx.room.TypeConverter

class ConvertidorTipos {

    @TypeConverter
    fun fromListString(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toListString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }
}