package com.example.tfg_sh.toDoList

import java.util.Date

class Tarea {
    var id = 0

    var nombre: String? = null
    var isRealizada = false
    var fecha_inicio: Date? = null
    var fecha_fin: Date? = null

    // constructores
    constructor() {}
    constructor(nombre: String?) {
        this.nombre = nombre
    }

    companion object {
        //public static void setPrioridad(prioridades prioridad) {
        val prioridad = arrayOf("baja", "normal", "importante", "muy_importante")
        //   this.prioridad = prioridad;
        //}
    }
}