package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.tfg_sh.R
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

        // creamos la lista inmutable de prioridades
        val prioridades = listOf("Alta", "Media", "Baja")
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(this, R.layout.dropdown_menu, prioridades)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)

        task.createPriority.prompt = "Selecciona una prioridad:"

        // relacionamos el adaptador con el spinner del xml
        task.createPriority.adapter = adaptador

        task.createPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Maneja el caso en el que no se haya seleccionado ninguna opción
            }
        }

        task.saveButton.setOnClickListener {
            if (task.createTitle.text.toString().trim { it <= ' ' }.isNotEmpty()
                && task.createPriority.selectedItem.toString().isNotEmpty()
            ) {
                val titulo = task.createTitle.getText().toString()
                val prioridad = task.createPriority.selectedItem.toString()
                DataObject.setDatos(titulo, prioridad)
                GlobalScope.launch {
                    bbdd.dao().insertTarea(ToDoList(0, titulo, prioridad))
                }

                val intent = Intent(this, ToDoListMain::class.java)
                // La flag asegurará que no se cree una nueva instancia de la actividad si ya se encuentra en la pila
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }

        task.buttonCerrar.setOnClickListener{ Utils.goToPreviousScreen(this) }
    }
}