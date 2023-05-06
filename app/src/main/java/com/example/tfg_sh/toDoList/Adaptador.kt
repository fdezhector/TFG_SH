package com.example.tfg_sh.toDoList

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R
import kotlinx.android.synthetic.main.view.view.*

class Adaptador(var data: List<CardViewInfo>) : RecyclerView.Adapter<Adaptador.viewHolder>() {
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo = itemView.title
        var prioridad = itemView.priority
        var layout = itemView.mylayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.view, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        when (data[position].prioridad.lowercase()) {
            "alta" -> holder.layout.setBackgroundColor(Color.parseColor("#F05454"))
            "media" -> holder.layout.setBackgroundColor(Color.parseColor("#EDC988"))
            else -> holder.layout.setBackgroundColor(Color.parseColor("#00917C"))
        }

        holder.titulo.text = data[position].titulo
        holder.prioridad.text = data[position].prioridad
        holder.itemView.setOnClickListener{
            val intent= Intent(holder.itemView.context, ActualizarCardView::class.java)
            intent.putExtra("id",position)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}