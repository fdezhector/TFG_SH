package com.example.tfg_sh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.databinding.ActivityPantallaInicioBinding
import com.example.tfg_sh.toDoList.ItemTarea
import com.example.tfg_sh.toDoList.ObjectTarea
import kotlinx.coroutines.launch


class PantallaInicio : AppCompatActivity() {

    private lateinit var main: ActivityPantallaInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityPantallaInicioBinding.inflate(layoutInflater)
        setContentView(main.root)
        //Instancia bbdd
        val dao = BetterYouBBDD.getInstance(this).betterYouDao
        lifecycleScope.launch {
            ObjectTarea.listaTareas = dao.getAllTareas() as MutableList<ItemTarea>
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}