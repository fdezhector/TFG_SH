package com.example.tfg_sh.bbdd

import androidx.room.TypeConverter

/**
 * Esta clase lo que hace es cuando se selecciona las distintas emociones antes de guardarlo
 * en la tabla del diario guardara un String con , y cuando cogemos por una consulta las distintas emociones
 * lo que hace es crear un map (clave|valor) con las distintas emociones que había
 */


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