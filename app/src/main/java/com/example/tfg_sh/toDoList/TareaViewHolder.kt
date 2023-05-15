package com.example.tfg_sh.toDoList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ItemTareaBinding

// El ViewHolder se encarga de mostrar los items del RecyclerView
class TareaViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val itemTarea = ItemTareaBinding.bind(view)
    
    fun render(tarea: ItemTarea){
        itemTarea.recyclerTitulo.text = tarea.titulo
        itemTarea.recyclerPrioridad.text = tarea.prioridad
    }
}
