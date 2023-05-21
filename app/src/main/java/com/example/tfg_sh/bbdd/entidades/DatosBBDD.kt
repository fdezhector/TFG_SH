package com.example.tfg_sh.bbdd.entidades

data class DatosBBDD(
    val diarios: List<Diario>,
    val eventos: List<Evento>,
    val notas: List<Nota>,
    val tareas: List<Tarea>
)
