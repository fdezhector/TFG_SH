package com.example.tfg_sh.bbdd.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tarea(
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    var titulo : String,
    var prioridad : String
)
