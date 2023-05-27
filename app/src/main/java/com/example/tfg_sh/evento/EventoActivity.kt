package com.example.tfg_sh.evento

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.R
import com.example.tfg_sh.Utils
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.databinding.ActivityEventoBinding
import com.example.tfg_sh.evento.timePicker.TimePickerFragment
import kotlinx.coroutines.launch
import java.util.Calendar

class EventoActivity : AppCompatActivity() {

    private lateinit var eventoBinding: ActivityEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventoBinding = ActivityEventoBinding.inflate(layoutInflater)
        setContentView(eventoBinding.root)
        val notaId = intent.getIntExtra("notaId", -1)
        var evento: Evento? = null
        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        lifecycleScope.launch {
            evento = dao.getEventoNotaId(notaId)
            cargarEvento(evento)
        }

        // TimePicker Hora Inicio y Hora Fin
        // Hora Inicio
        eventoBinding.layoutFechaInicio.setOnClickListener {
            showTimePickerDialog(eventoBinding.textHoraFechaInicio)
        }
        // Hora Fin
        eventoBinding.layoutFechaFin.setOnClickListener {
            showTimePickerDialog(eventoBinding.textHoraFechaFin)
        }

        eventoBinding.buttonGuardar.setOnClickListener {
            //FIXME Refactorizar esto y optimizar en un método
            //Comprobaciones para los campos
            if (eventoBinding.titulo.text.isNullOrEmpty()) {
                Toast.makeText(this, "El evento debe tener un título", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (eventoBinding.textHoraFechaInicio.text.isNullOrEmpty() && eventoBinding.textHoraFechaFin.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "Tienes que tener seleccionada una hora de inicio/fin",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            //Comprobamos la seleccion de la fecha sea correcta
            val horaInicio = Utils.obtenerHora(eventoBinding.textHoraFechaInicio.text.toString())
            val horaFin = Utils.obtenerHora(eventoBinding.textHoraFechaFin.text.toString())

            if (horaInicio > horaFin && horaFin < horaInicio) {
                Toast.makeText(this, "La hora de fin debe ser posterior a la hora de inicio", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            actualizarEvento(dao, notaId, evento)
            Utils.goToMainScreen(this)
            Toast.makeText(
                this,
                "Evento guardado: " + eventoBinding.titulo.text.toString(),
                Toast.LENGTH_LONG
            ).show()

            Utils.scheduleNotificacion(
                this,
                "Evento BetterYou",
                "Evento: " + eventoBinding.titulo.text.toString() + " esta a punto de comenzar",
                "Tu evento comienza a las " + eventoBinding.textHoraFechaInicio.text.toString(),
                ((horaInicio.time-900000) - Calendar.getInstance().timeInMillis).toInt()
            )
            Utils.scheduleNotificacion(
                this,
                "Evento BetterYou",
                "Evento: " + eventoBinding.titulo.text.toString() + " esta a punto de finalizar",
                "Tu evento finaliza a las " + eventoBinding.textHoraFechaFin.text.toString(),
                ((horaFin.time-900000) - Calendar.getInstance().timeInMillis).toInt()
            )
        }
        eventoBinding.buttonBorrar.setOnClickListener {
            borrarEvento(dao, evento, notaId)
        }
        eventoBinding.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

        actualizarContadorPalabras()
    }

    private fun borrarEvento(dao: BetterYouDao, evento: Evento?, notaId: Int) {
        val alertDialog = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        alertDialog.setView(customView)
        val dialog = alertDialog.create()
        //Elementos alertDialog
        val titulo = customView.findViewById<TextView>(R.id.Title)
        val mensaje = customView.findViewById<TextView>(R.id.Message)
        val aceptar = customView.findViewById<Button>(R.id.PositiveButton)
        val cancelar = customView.findViewById<Button>(R.id.NegativeButton)

        aceptar.setOnClickListener {

            lifecycleScope.launch {
                dao.updateEvento(
                    Evento(
                        id = evento!!.id,
                        titulo = null,
                        descripcion = null,
                        ubicacion = null,
                        horaInicio = null,
                        horaFin = null,
                        notaId = notaId
                    )
                )
            }
            Utils.goToMainScreen(this)
            dialog.dismiss()
            Toast.makeText(
                this,
                "Se ha borrado " + eventoBinding.titulo.text.toString() + " el evento correctamente",
                Toast.LENGTH_LONG
            ).show()
        }

        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        titulo.text = "Eliminar evento "
        mensaje.text = "¿Quieres borrar el evento actual?"

        dialog.show()
    }

    private fun cargarEvento(evento: Evento?) {
        if (evento != null) {
            eventoBinding.titulo.setText(evento.titulo)
            eventoBinding.descripcion.setText(evento.descripcion)
            eventoBinding.ubicacion.setText(evento.ubicacion)
            eventoBinding.textHoraFechaInicio.setText(evento.horaInicio)
            eventoBinding.textHoraFechaFin.setText(evento.horaFin)
        }
    }


    private fun actualizarEvento(dao: BetterYouDao, notaId: Int, evento: Evento?) {

        val titulo: String = eventoBinding.titulo.text.toString()
        val ubicacion: String = eventoBinding.ubicacion.text.toString()
        val horaInicio: String = eventoBinding.textHoraFechaInicio.text.toString()
        val horaFin: String = eventoBinding.textHoraFechaFin.text.toString()
        val descripcion: String = eventoBinding.descripcion.text.toString()

        lifecycleScope.launch {
            if (evento != null) {
                dao.updateEvento(
                    Evento(
                        id = evento.id,
                        titulo = titulo,
                        descripcion = descripcion,
                        ubicacion = ubicacion,
                        horaInicio = horaInicio,
                        horaFin = horaFin,
                        notaId = notaId
                    )
                )
            }
        }
    }

    // TimePicker
    private fun showTimePickerDialog(textView: TextView) {
        val timePicker = TimePickerFragment { onTimeSelected(it, textView) }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String, textView: TextView) {
        textView.setText("$time")
    }

    private fun actualizarContadorPalabras() {
        val longitudMaxima = 200

        eventoBinding.descripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(texto: Editable?) {
                // Obtenemos la longitud actual de la descripción aún si 'texto' es null utilizando '?'
                val longitudTexto = texto?.length ?: 0
                eventoBinding.contadorPalabras.text = "$longitudTexto / $longitudMaxima"
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