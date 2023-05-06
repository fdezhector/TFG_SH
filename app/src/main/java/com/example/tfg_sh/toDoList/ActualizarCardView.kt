package com.example.tfg_sh.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.example.tfg_sh.Utils
import com.example.tfg_sh.databinding.ActivityActualizarCardViewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActualizarCardView : AppCompatActivity() {
    private lateinit var task: ActivityActualizarCardViewBinding
    private lateinit var bbdd: BBDDToDoList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = ActivityActualizarCardViewBinding.inflate(layoutInflater)
        setContentView(task.root)
        bbdd = Room.databaseBuilder(
            applicationContext, BBDDToDoList::class.java, "To_Do"
        ).build()

        val pos = intent.getIntExtra("id", -1)

        if (pos != -1) {
            val titulo = DataObject.getDatos(pos).titulo
            val prioridad = DataObject.getDatos(pos).prioridad
            task.createTitle.setText(titulo)
            task.createPriority.setText(prioridad)

            task.deleteButton.setOnClickListener {
                DataObject.borrarUno(pos)
                GlobalScope.launch {
                    bbdd.dao().deleteTarea(
                        ToDoList
                            (
                            pos + 1,
                            task.createTitle.text.toString(),
                            task.createPriority.text.toString()
                        )
                    )
                }
                Toast.makeText(this, "Se ha borrado la tarea correctamente", Toast.LENGTH_LONG)
                    .show()
                volverToDoListMain()
            }

            task.updateButton.setOnClickListener {
                DataObject.actualizarDatos(
                    pos,
                    task.createTitle.text.toString(),
                    task.createPriority.text.toString()
                )
                GlobalScope.launch {
                    bbdd.dao().updateTarea(
                        ToDoList
                            (
                            pos + 1, task.createTitle.text.toString(),
                            task.createPriority.text.toString()
                        )
                    )
                }
                Toast.makeText(this, "Se ha actualizado la tarea correctamente", Toast.LENGTH_LONG)
                    .show()
                volverToDoListMain()
            }
        }


        task.buttonCerrar.setOnClickListener{ Utils.goToPreviousScreen(this) }

    }

    fun volverToDoListMain() {
        val intent = Intent(this, ToDoListMain::class.java)
        startActivity(intent)
    }
}