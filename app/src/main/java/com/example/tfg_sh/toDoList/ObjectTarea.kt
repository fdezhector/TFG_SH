package com.example.tfg_sh.toDoList

object ObjectTarea {

    var listaTareas = mutableListOf<ItemTarea>()

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