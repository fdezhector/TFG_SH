package com.example.tfg_sh.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.tfg_sh.Utils
import com.example.tfg_sh.databinding.ActivityCrearCardViewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearCardView : AppCompatActivity() {
    private lateinit var task: ActivityCrearCardViewBinding
    private lateinit var bbdd: BBDDToDoList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = ActivityCrearCardViewBinding.inflate(layoutInflater)
        setContentView(task.root)
        //Crear BBDD
        bbdd = Room.databaseBuilder(
            applicationContext, BBDDToDoList::class.java, "To_Do"
        ).build()

        task.saveButton.setOnClickListener {
            if (task.createTitle.text.toString().trim { it <= ' ' }.isNotEmpty()
                && task.createPriority.text.toString().trim { it <= ' ' }.isNotEmpty()
            ) {
                val titulo = task.createTitle.getText().toString()
                val prioridad = task.createPriority.getText().toString()
                DataObject.setDatos(titulo, prioridad)
                GlobalScope.launch {
                    bbdd.dao().insertTarea(ToDoList(0, titulo, prioridad))
                }

                val intent = Intent(this, ToDoListMain::class.java)
                startActivity(intent)

            }
        }

        task.buttonCerrar.setOnClickListener{ Utils.goToPreviousScreen(this) }
    }
}