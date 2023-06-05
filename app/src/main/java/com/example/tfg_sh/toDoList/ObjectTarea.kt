package com.example.tfg_sh.toDoList

/**
 * Este sera el objeto auxiliar con el que trabajaremos como intermediario entre la entidad Tarea y la BBDD
 */
object ObjectTarea {

    // Lista principal donde guardaremos las tareas que se visualizaran en el recyclerView
    var listaTareas = mutableListOf<ItemTarea>()
    // Lista auxiliar que nos servirá como copia de seguridad a la hora de filtrar
    var listaAuxiliar = mutableListOf<ItemTarea>()

    fun getTarea(pos:Int): ItemTarea {
        return listaTareas[pos]
    }

    fun addTarea(id:Long, title: String, priority: String) {
        listaTareas.add(ItemTarea(id, title, priority))
    }

    fun updateTarea(pos:Int, titulo:String, prioridad:String) {
        listaTareas[pos].titulo = titulo
        listaTareas[pos].prioridad = prioridad
    }

    fun deleteTarea(pos:Int) {
        listaTareas.removeAt(pos)
    }

    fun getAll(): List<ItemTarea> {
        return listaTareas
    }

    fun deleteAll() {
        listaTareas.clear()
    }

}