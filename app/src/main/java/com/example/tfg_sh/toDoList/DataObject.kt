package com.example.tfg_sh.toDoList


object DataObject {
    var listaDatos = mutableListOf<CardViewInfo>()

    fun setDatos(titulo:String,prioridad:String){
        listaDatos.add(CardViewInfo(titulo,prioridad))
    }

    fun getTodosLosDatos():List<CardViewInfo>{
        return listaDatos
    }

    fun borrarTodos(){
        listaDatos.clear()
    }

    fun getDatos(pos:Int):CardViewInfo{
        return listaDatos[pos]
    }

    fun borrarUno(pos: Int){
        listaDatos.removeAt(pos)
    }

    fun actualizarDatos(pos:Int,titulo: String,prioridad: String){
        listaDatos[pos].titulo = titulo
        listaDatos[pos].prioridad = prioridad
    }


}