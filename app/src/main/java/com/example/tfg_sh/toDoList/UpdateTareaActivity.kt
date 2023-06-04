package com.example.tfg_sh.toDoList

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityUpdateTareaBinding
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

class UpdateTareaActivity : AppCompatActivity() {

    private lateinit var updateTarea: ActivityUpdateTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTarea = ActivityUpdateTareaBinding.inflate(layoutInflater)
        setContentView(updateTarea.root)
        // Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // Cargamos el dropdown menu con las prioridades
        Utils.initDropDownMenu(updateTarea.prioridad,this)

        // obtenemos la posición de la tarea en la lista y si no trae información ponemos por defecto -1
        val position = intent.getIntExtra("position", -1)
        // Obtenemos el objeto Tarea
        val tarea = ObjectTarea.getTarea(position)
        //Si hay alguna tarea
        if(position != -1){
            val titulo = tarea.titulo
            val prioridad = tarea.prioridad
            updateTarea.titulo.setText(titulo)
            updateTarea.prioridad.setSelection(Utils.prioridades.indexOf(prioridad))

            updateTarea.updateButton.setOnClickListener {
                if(updateTarea.titulo.text.isNullOrEmpty()) {
                    StyleableToast.makeText(this, "La tarea debe tener un título", Toast.LENGTH_LONG, R.style.toast_by).show()
                    return@setOnClickListener
                }
                updateTarea(tarea, position, dao)
                StyleableToast.makeText(this, "La tarea se ha actualizado correctamente", Toast.LENGTH_LONG, R.style.toast_by).show()
                Utils.goToMainScreen(this)
            }

            updateTarea.deleteButton.setOnClickListener {
                deleteTarea(tarea, position, dao)
                StyleableToast.makeText(this, tarea.titulo + " se ha borrado correctamente", Toast.LENGTH_LONG, R.style.toast_by).show()
                Utils.goToMainScreen(this)
            }
        }

        updateTarea.buttonCerrar.setOnClickListener {
            Utils.goToMainScreen(this)
        }
    }

    private fun updateTarea(tarea: ItemTarea, position: Int, dao: BetterYouDao){
        //Obtenemos los nuevos valores
        val titulo = updateTarea.titulo.text.toString()
        val prioridad = updateTarea.prioridad.selectedItem.toString()
        //Actualizamos tanto el objeto en la lista como en la base de datos
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