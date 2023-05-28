package com.example.tfg_sh.diario

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_sh.databinding.ItemEmotionBinding

class EmocionesViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val itemEmocion = ItemEmotionBinding.bind(view)

    fun render(emocion: ItemEmocion){
        itemEmocion.itemRecyclerEmotion.text = emocion.emocion

    }

}