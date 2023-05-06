package com.example.tfg_sh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.example.tfg_sh.toDoList.BBDDToDoList
import com.example.tfg_sh.toDoList.CardViewInfo
import com.example.tfg_sh.toDoList.DataObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PantallaInicio : AppCompatActivity() {
    private lateinit var bbdd: BBDDToDoList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)
        bbdd = Room.databaseBuilder(
            applicationContext, BBDDToDoList::class.java, "To_Do"
        ).build()
        GlobalScope.launch {
            DataObject.listaDatos = bbdd.dao().getTareas() as MutableList<CardViewInfo>
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}