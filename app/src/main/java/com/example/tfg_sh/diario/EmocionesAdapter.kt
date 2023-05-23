package com.example.tfg_sh.diario

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.R

class EmocionesAdapter(private val listaEmociones: List<ItemEmocion>) : RecyclerView.Adapter<EmocionesViewHolder>() {
    // TODO comentar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmocionesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EmocionesViewHolder(layoutInflater.inflate(R.layout.item_emotion, parent, false))
    }

    override fun onBindViewHolder(holder: EmocionesViewHolder, position: Int) {
        val item = listaEmociones[position]
        holder.render(item)

        /*holder.itemView.setOnClickListener {
            // todo eliminar emocion
        }*/

    }

    override fun getItemCount(): Int {
        return listaEmociones.size
    }
}