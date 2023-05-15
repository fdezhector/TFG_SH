package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.R
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityInsertTareaBinding
import kotlinx.coroutines.launch

class InsertTareaActivity : AppCompatActivity() {

    private lateinit var insertTarea: ActivityInsertTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        insertTarea = ActivityInsertTareaBinding.inflate(layoutInflater)
        setContentView(insertTarea.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // creamos la lista inmutable de prioridades
        val prioridades = listOf("Alta", "Media", "Baja")
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(this, R.layout.dropdown_menu, prioridades)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)

        // relacionamos el adaptador con el spinner del xml
        insertTarea.prioridad.adapter = adaptador

        insertTarea.prioridad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Maneja el caso en el que no se haya seleccionado ninguna opción
            }
        }

        insertTarea.saveButton.setOnClickListener{
            // comprobaciones (siempre habrá una prioridad seleccionada por defecto)
            if(insertTarea.titulo.text.isEmpty()){
                Toast.makeText(this, "La tarea debe tener un título", Toast.LENGTH_LONG).show()
            } else {
                var titulo = insertTarea.titulo.text.toString()
                var prioridad = insertTarea.prioridad.selectedItem.toString()

                ObjectTarea.addTarea(titulo, prioridad)
                lifecycleScope.launch {
                    var tarea = Tarea(titulo = titulo, prioridad = prioridad)
                    dao.insertTarea(tarea)
                }
                val intent = Intent(this, ToDoListMain::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }

        }

    }
}