package com.example.tfg_sh.toDoList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.tfg_sh.R
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

        // creamos la lista inmutable de prioridades
        val prioridades = listOf("Alta", "Media", "Baja")
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(this, R.layout.dropdown_menu, prioridades)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)

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

        if (pos != -1) {
            val titulo = DataObject.getDatos(pos).titulo
            val prioridad = DataObject.getDatos(pos).prioridad
            task.createTitle.setText(titulo)
            task.createPriority.setSelection(prioridades.indexOf(prioridad))

            // DELETE
            task.deleteButton.setOnClickListener {
                DataObject.borrarUno(pos)
                GlobalScope.launch {
                    bbdd.dao().deleteTarea(
                        ToDoList
                            (
                            pos + 1,
                            task.createTitle.text.toString(),
                            task.createPriority.selectedItem.toString()
                        )
                    )
                }
                Toast.makeText(this, "Se ha borrado la tarea correctamente", Toast.LENGTH_LONG)
                    .show()
                volverToDoListMain()
            }

            // UPDATE
            task.updateButton.setOnClickListener {
                DataObject.actualizarDatos(
                    pos,
                    task.createTitle.text.toString(),
                    task.createPriority.selectedItem.toString()
                )
                GlobalScope.launch {
                    bbdd.dao().updateTarea(
                        ToDoList (
                            pos + 1, task.createTitle.text.toString(),
                            task.createPriority.selectedItem.toString()
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
        // La flag asegurará que no se cree una nueva instancia de la actividad si ya se encuentra en la pila
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }
}