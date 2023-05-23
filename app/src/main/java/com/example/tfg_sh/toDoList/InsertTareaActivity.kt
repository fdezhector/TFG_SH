package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityInsertTareaBinding
import kotlinx.coroutines.launch
import java.util.Date

class InsertTareaActivity : AppCompatActivity() {

    private lateinit var insertTarea: ActivityInsertTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        insertTarea = ActivityInsertTareaBinding.inflate(layoutInflater)
        setContentView(insertTarea.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // creamos el dropdown menu con las prioridades
        Utils.initDropDownMenu(insertTarea.prioridad, this)

        // Operación INSERT
        insertTarea.saveButton.setOnClickListener {
            // comprobaciones (siempre habrá una prioridad seleccionada por defecto)
            if(insertTarea.titulo.text.isNullOrEmpty()) {
                Toast.makeText(this, "La tarea debe tener un título", Toast.LENGTH_LONG).show()
            } else {
                var id = Date().time as Long
                var titulo = insertTarea.titulo.text.toString()
                var prioridad = insertTarea.prioridad.selectedItem.toString()
                lifecycleScope.launch {
                    val tarea = Tarea(id = id, titulo = titulo, prioridad = prioridad)
                    dao.insertTarea(tarea)
                }
                ObjectTarea.addTarea(id, titulo, prioridad)
                val intent = Intent(this, ToDoListMain::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }

        }

        // FIXME mirar cómo mejorar la navegación hacia atrás de las pantallas
        insertTarea.buttonCerrar.setOnClickListener {
            Utils.goToMainScreen(this)
        }

    }// onCreate()
}