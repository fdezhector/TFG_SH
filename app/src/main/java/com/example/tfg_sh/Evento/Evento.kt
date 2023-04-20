package com.example.tfg

import java.util.Date

class Evento {
    //Atributos
    private var titulo: String? = null
    private var ubicacion: String? = null
    private var descripcion: String? = null
    private var fecha_inicio: Date? = null
    private var fecha_fin: Date? = null

    //To-do Recordatorio notificacion
    constructor() {}
    constructor(titulo: String?, fecha_inicio: Date?, fecha_fin: Date?) {
        this.titulo = titulo
        this.fecha_inicio = fecha_inicio
        this.fecha_fin = fecha_fin
    }

    constructor(
        titulo: String?,
        ubicacion: String?,
        descripcion: String?,
        fecha_inicio: Date?,
        fecha_fin: Date?
    ) {
        this.titulo = titulo
        this.ubicacion = ubicacion
        this.descripcion = descripcion
        this.fecha_inicio = fecha_inicio
        this.fecha_fin = fecha_fin
    }
}