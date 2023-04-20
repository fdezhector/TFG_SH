package com.example.tfg

import java.text.SimpleDateFormat
import java.util.Date

class Nota {
    //getters / setters
    val id: Int
    val fecha_nota: Date
    val lista_toDoList: ArrayList<ToDoList>
    val diario: Diario
    val evento: Evento

    // constructores
    init {
        fecha_nota = Date()
        id = setId(fecha_nota)
        lista_toDoList = ArrayList()
        diario = Diario()
        evento = Evento()
    }

    fun setId(fecha_nota: Date?): Int {
        // el id será la fecha del día de la nota
        // por ejemplo: 5 de abril de 2023 --> id = 5042023
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val fecha = sdf.format(fecha_nota)
        return fecha.replace("/".toRegex(), "").toInt()
    }
}