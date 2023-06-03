package com.example.tfg_sh.toDoList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R

/**
 * Clase que nos va a permitir cargar el recyclerView
 */
class  TareaAdapter(private val listaTareas: List<ItemTarea>, private val context: Context) : RecyclerView.Adapter<TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TareaViewHolder(layoutInflater.inflate(R.layout.item_tarea, parent, false), context)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val item = listaTareas[position]
        holder.render(item)

        // Listener que nos llevara a la actividad para actualizar la tarea que se ha pulsado
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateTareaActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }
}