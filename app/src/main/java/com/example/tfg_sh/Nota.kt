package com.example.tfg_sh

import com.example.tfg.Diario
import com.example.tfg_sh.evento.Evento
import com.example.tfg_sh.toDoList.ToDoList
import java.text.SimpleDateFormat
import java.util.Date

class Nota {
    //getters / setters
    private val id: Int
    private val fecha_nota: Date = Date()
    private val lista_toDoList: ArrayList<ToDoList>
    private val diario: Diario
    private val evento: Evento

    // constructores
    init {
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