package com.example.tfg_sh.toDoList

import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityUpdateTareaBinding
import kotlinx.coroutines.launch

class UpdateTareaActivity : AppCompatActivity() {

    private lateinit var updateTarea: ActivityUpdateTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTarea = ActivityUpdateTareaBinding.inflate(layoutInflater)
        setContentView(updateTarea.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // creamos el dropdown menu con las prioridades
        Utils.initDropDownMenu(updateTarea.prioridad,this)

        // obtenemos la posición de la tarea en la lista
        val position = intent.getIntExtra("position", -1)
        val tarea = ObjectTarea.getTarea(position)

        if(position != -1){
            var titulo = tarea.titulo
            var prioridad = tarea.prioridad
            updateTarea.titulo.setText(titulo)
            updateTarea.prioridad.setSelection(Utils.prioridades.indexOf(prioridad))

            updateTarea.updateButton.setOnClickListener {
                updateTarea(tarea, position, dao)
                Toast.makeText(this, "La tarea se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                // FIXME cambiar a que vaya a ToDoList Main
                Utils.goToMainScreen(this)
            }

            updateTarea.deleteButton.setOnClickListener {
                deleteTarea(tarea, position, dao)
                Toast.makeText(this, tarea.titulo + " se ha borrado correctamente", Toast.LENGTH_LONG).show()
                // FIXME cambiar a que vaya a ToDoList Main
                Utils.goToMainScreen(this)
            }
        }


        // TODO mirar cómo mejorar la navegación hacia atrás de las pantallas
        updateTarea.buttonCerrar.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }

    private fun updateTarea(tarea: ItemTarea, position: Int, dao: BetterYouDao){
        // obtenemos los nuevos valores
        var titulo = updateTarea.titulo.text.toString()
        var prioridad = updateTarea.prioridad.selectedItem.toString()
        // actualizamos tanto el objeto en la lista como en la base de datos
        ObjectTarea.updateTarea(position, titulo, prioridad)
        lifecycleScope.launch {
            dao.updateTarea(Tarea(tarea.id, titulo, prioridad))
        }
    }// updateTarea()

    private fun deleteTarea(tarea: ItemTarea, position: Int, dao: BetterYouDao){
        ObjectTarea.deleteTarea(position)
        lifecycleScope.launch {
            dao.deleteTarea(Tarea(tarea.id, tarea.titulo, tarea.prioridad))
        }
    }// deleteTarea()
}