package com.example.tfg_sh.bbdd.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Nota(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val fecha: String
)
