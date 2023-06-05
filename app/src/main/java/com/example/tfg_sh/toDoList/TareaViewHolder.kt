package com.example.tfg_sh.toDoList

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R
import com.example.tfg_sh.databinding.ItemTareaBinding

// El ViewHolder se encarga de mostrar los items del RecyclerView
class TareaViewHolder(view : View, private val context: Context) : RecyclerView.ViewHolder(view){
    val itemTarea = ItemTareaBinding.bind(view)
    
    fun render(tarea: ItemTarea){
        itemTarea.borderColorPrioridad.setBackgroundColor(evaluarColorBorde(tarea.prioridad))
        itemTarea.recyclerTitulo.text = tarea.titulo
        itemTarea.recyclerPrioridad.text = tarea.prioridad
    }

    private fun evaluarColorBorde(prioridad : String): Int{
        val colorBorde: Int =
        if (prioridad.equals("Alta", ignoreCase = true)) {
            ContextCompat.getColor(context, R.color.prioridad_alta_1)
        } else if (prioridad.equals("Media", ignoreCase = true)) {
            ContextCompat.getColor(context, R.color.prioridad_media_1)
        } else {
            ContextCompat.getColor(context, R.color.colorContainer)
        }
        return colorBorde
    }
}
