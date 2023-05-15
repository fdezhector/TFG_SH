package com.example.tfg_sh.toDoList

object ObjectTarea {

    var listaTareas = mutableListOf<ItemTarea>()

    fun addTarea(title: String, priority: String) {
        listaTareas.add(ItemTarea(title, priority))
    }

    fun getTarea(pos:Int): ItemTarea {
        return listaTareas[pos]
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

    fun updateTarea(pos:Int, titulo:String, prioridad:String) {
        listaTareas[pos].titulo = titulo
        listaTareas[pos].prioridad = prioridad
    }

}