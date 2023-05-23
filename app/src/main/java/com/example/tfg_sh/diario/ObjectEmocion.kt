package com.example.tfg_sh.diario

object ObjectEmocion {

    var listaEmociones = mutableListOf<ItemEmocion>()

    fun getEmocion(pos:Int): ItemEmocion {
        return listaEmociones[pos]
    }

    fun addEmocion(emocion: String) {
        listaEmociones.add(ItemEmocion(emocion))
    }

    fun deleteTarea(pos:Int) {
        listaEmociones.removeAt(pos)
    }

    fun getAll(): List<ItemEmocion> {
        return listaEmociones
    }

}