package com.example.tfg_sh.bbdd.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Nota::class,
            parentColumns = ["id"],
            childColumns = ["notaId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class Diario(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    var etiquetas: String?,
    var descripcion: String?,
    val notaId: Int
)