package com.example.tfg

import java.util.Date
import kotlin.collections.ArrayList

class ToDoList {
    // getters / setters
    var titulo: String? = null
    var categoria: String? = null
    var lista_tareas: ArrayList<Tarea>

    // constructores
    constructor() {
        lista_tareas = ArrayList()
    }

    constructor(titulo: String?, categoria: String?) {
        this.titulo = titulo
        this.categoria = categoria
        lista_tareas = ArrayList()
    }

    fun crearTarea(nombre: String?, fecha_inicio: Date?, fecha_fin: Date?, prioridad: String?) {
        val tarea = Tarea(nombre)
        val id = lista_tareas.size + 1
        tarea.id = id
        tarea.fecha_inicio = fecha_inicio
        tarea.fecha_fin = fecha_fin
        tarea.isRealizada = false

        //Mirar lo de prioridad
        lista_tareas.add(tarea)
    }

    fun editarTarea(tarea: Tarea, nombre: String?, fecha_inicio: Date?, fecha_fin: Date?, prioridad: String?) {
        tarea.nombre = nombre
        tarea.fecha_inicio = fecha_inicio
        tarea.fecha_fin = fecha_fin
        //Mirar lo de la prioridad
    }

    fun eliminarTarea(tarea: Tarea) {
        lista_tareas.remove(tarea)
    }
}