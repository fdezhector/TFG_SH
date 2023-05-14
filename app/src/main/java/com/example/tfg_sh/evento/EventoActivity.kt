package com.example.tfg_sh.evento

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.Utils
import com.example.tfg_sh.databinding.ActivityEventoBinding
import com.example.tfg_sh.evento.timePicker.TimePickerFragment

class EventoActivity : AppCompatActivity() {

    private lateinit var evento: ActivityEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        evento = ActivityEventoBinding.inflate(layoutInflater)
        setContentView(evento.root)


        // TimePicker Hora Inicio y Hora Fin
        // Hora Inicio
        evento.layoutFechaInicio.setOnClickListener{
            showTimePickerDialog(evento.textHoraFechaInicio)
        }
        // Hora Fin
        evento.layoutFechaFin.setOnClickListener{
            showTimePickerDialog(evento.textHoraFechaFin)
        }




        // funcionalidades botones principales
        evento.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

        // funcionalidades secundarias
        // TODO mirar si meterlo en un Utils
        actualizarContadorPalabras()
    }

    // TimePicker
    private fun showTimePickerDialog(textView: TextView) {
        val timePicker = TimePickerFragment { onTimeSelected(it, textView) }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String, textView: TextView) {
        textView.setText("$time")
    }

    private fun actualizarContadorPalabras(){
        val longitudMaxima = 200

        evento.descripcion.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(texto: Editable?) {
                // Obtenemos la longitud actual de la descripción aún si 'texto' es null utilizando '?'
                var longitudTexto = texto?.length ?: 0
                evento.contadorPalabras.text = "$longitudTexto / $longitudMaxima"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }
        })
    }
}