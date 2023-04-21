package com.example.tfg_sh

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.databinding.ActivityMainBinding
import com.example.tfg_sh.diario.DiarioActivity
import com.example.tfg_sh.evento.EventoActivity
import com.example.tfg_sh.toDoList.ToDoListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var main: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        main.vistaEvento.setOnClickListener {
            val vistaEventoActivty: Intent = Intent(applicationContext, EventoActivity::class.java)
            startActivity(vistaEventoActivty)
        }

        main.vistaDiario.setOnClickListener {
            val vistaDiarioActivity: Intent = Intent(applicationContext, DiarioActivity::class.java)
            startActivity(vistaDiarioActivity)
        }

        main.vistaToDoList.setOnClickListener {
            val vistaToDoListActivity: Intent = Intent(applicationContext, ToDoListActivity::class.java)
            startActivity(vistaToDoListActivity)
        }


    }

}