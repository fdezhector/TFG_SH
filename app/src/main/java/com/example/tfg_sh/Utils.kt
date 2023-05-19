package com.example.tfg_sh

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.reflect.KClass

object Utils {
    // FIXME mirar el tema de las flags y este método
    /*fun goToPreviousScreen(activity: Activity, clase: KClass<*>){
        val intent = Intent(activity, clase::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }*/

    // creamos la lista inmutable de prioridades
    val prioridades = listOf("Alta", "Media", "Baja")

    fun goToMainScreen(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }

    fun setId(fecha_nota: Date?): Int {
        // el id será la fecha del día de la nota
        // por ejemplo: 5 de abril de 2023 --> id = 5042023
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val fecha = sdf.format(fecha_nota)
        return fecha.replace("/".toRegex(), "").toInt()
    }

    fun obtenerFechaNota(year: Int, month: Int, dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

    fun initDropDownMenu(dropDown: Spinner, context: Context) {
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(context, R.layout.dropdown_menu, prioridades)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)

        // relacionamos el adaptador con el spinner del xml
        dropDown.adapter = adaptador

        // listener que se ejectutará cuando se ha seleccionado una prioridad
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String

                val dropDownShownItem = (context as Activity).findViewById<CheckedTextView>(R.id.dropdown_menu_layout)
                // evaluamos qué color se deberá mostrar cuando seleccionemos una prioridad
                dropDownShownItem.setBackgroundColor(evaluarColor(selectedItem, context))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // NO APLICA | Maneja el caso en el que no se haya seleccionado ninguna opción
            }
        }
    }

    private fun evaluarColor(selectedItem: String, context: Context): Int {
        var dropDownColor: Int
        if (selectedItem.equals("Alta", ignoreCase = true)) {
            dropDownColor = ContextCompat.getColor(context, R.color.prioridad_alta_3)
        } else if (selectedItem.equals("Media", ignoreCase = true)) {
            dropDownColor = ContextCompat.getColor(context, R.color.prioridad_media_2)
        } else {
            dropDownColor = ContextCompat.getColor(context, R.color.prioridad_baja_3)
        }
        return dropDownColor
    }
}