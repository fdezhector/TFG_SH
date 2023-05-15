package com.example.tfg_sh.toDoList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R

class TareaAdapter(private val listaTareas: List<ItemTarea>) : RecyclerView.Adapter<TareaViewHolder>() {
    // TODO comentar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TareaViewHolder(layoutInflater.inflate(R.layout.item_tarea, parent, false))
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val item = listaTareas[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaTareas.size
    }
}