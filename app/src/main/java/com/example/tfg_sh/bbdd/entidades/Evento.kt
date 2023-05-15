package com.example.tfg_sh.bbdd.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Nota::class,
            parentColumns = ["id"],
            childColumns = ["notaId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    var titulo: String?,
    var descripcion: String?,
    var ubicacion: String?,
    var horaInicio: String?,
    var horaFin: String?,
    //FOREING KEY DE NOTA
    val notaId:Int
)
