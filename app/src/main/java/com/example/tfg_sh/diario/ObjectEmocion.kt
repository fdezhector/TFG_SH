package com.example.tfg_sh.diario

object ObjectEmocion {

    var listaEmociones = mutableListOf<ItemEmocion>()
    var listaEmocionesMarcadas = mutableListOf<ItemEmocion>()

    fun getAll(): List<ItemEmocion> {
        return listaEmociones
    }

}