package com.example.tfg

class Diario {
    //Getter y Setter
    //To-do Color
    //Atributos
    private var descripcion: String? = null
    private var etiquetas: ArrayList<String>? = null

    // constructores
    constructor() {}

    constructor(descripcion: String?, etiquetas: ArrayList<String>?) {
        this.descripcion = descripcion
        this.etiquetas = etiquetas
    }

}