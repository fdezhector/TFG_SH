package com.example.tfg_sh.bbdd.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tfg_sh.bbdd.ConvertidorTipos

@Entity(
    foreignKeys = [
        ForeignKey(entity = Nota::class,
            parentColumns = ["id"],
            childColumns = ["notaId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
@TypeConverters(ConvertidorTipos::class)
data class Diario(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    var emociones: List<String>?,
    var descripcion: String?,
    val notaId: Int
)