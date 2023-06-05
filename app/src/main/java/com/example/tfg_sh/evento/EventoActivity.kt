package com.example.tfg_sh.evento

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

class EventoActivity : AppCompatActivity() {

    private lateinit var eventoBinding: ActivityEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventoBinding = ActivityEventoBinding.inflate(layoutInflater)
        setContentView(eventoBinding.root)
        // Recibimos el ID de la nota a la que pertenece el Evento
        val notaId = intent.getIntExtra("notaId", -1)
        var evento: Evento? = null
        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        // Hacemos la consulta a la BBDD para obtener el objeto Evento
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
            // Validaciones tanto del titulo como de la hora de inicio y la hora de fin
            if (eventoBinding.titulo.text.isNullOrEmpty()) {
                StyleableToast.makeText(this, "El evento debe tener un título", Toast.LENGTH_LONG, R.style.toast_by).show()
                return@setOnClickListener
            } else if (eventoBinding.textHoraFechaInicio.text.isNullOrEmpty() && eventoBinding.textHoraFechaFin.text.isNullOrEmpty()) {
                StyleableToast.makeText(this, "Tienes que te seleccionar una hora de inicio/fin", Toast.LENGTH_LONG, R.style.toast_by).show()
                return@setOnClickListener
            }

            //Comprobamos que la hora de fin no sea anterior a la hora de inicio
            val horaInicioString = eventoBinding.textHoraFechaInicio.text.toString()
            val horaFinString = eventoBinding.textHoraFechaFin.text.toString()

            val horaInicioTime = Utils.obtenerHora(horaInicioString)
            val horaFinTime = Utils.obtenerHora(horaFinString)

            if (horaInicioTime > horaFinTime && horaFinTime < horaInicioTime) {
                StyleableToast.makeText(this, "La hora de fin debe ser posterior a la hora de inicio", Toast.LENGTH_LONG, R.style.toast_by).show()
                return@setOnClickListener
            }

            actualizarEvento(dao, notaId, evento)
            Utils.goToMainScreen(this)
            StyleableToast.makeText(this, "Evento guardado: " + eventoBinding.titulo.text.toString(), Toast.LENGTH_LONG, R.style.toast_by).show()

            // Programamos la notificacion 15 minutos antes de que empiece el evento
            val eventoNotificacionService = EventoNotificacionService(this)

            var fecha = evento!!.notaId.toString()

            if(fecha.length < 8){
                fecha = "0$fecha"
            }

            val anno = fecha.substring(fecha.length - 4, fecha.length).toInt()
            val mes = fecha.substring(2, 4).toInt()
            val dia = fecha.substring(0, 2).toInt()
            val hora = horaInicioString.substring(0, 2).toInt()
            val minuto = horaInicioString.substring(3, 5).toInt()
            val titulo = "BetterYou Evento"
            val texto = "${eventoBinding.titulo.text} está a punto de comenzar"

            eventoNotificacionService.programarNotificacion(anno, mes, dia, hora, minuto, titulo, texto)

        }

        eventoBinding.buttonBorrar.setOnClickListener {
            borrarEvento(dao, evento, notaId)
        }

        eventoBinding.buttonCerrar.setOnClickListener { Utils.goToMainScreen(this) }

        // La descripcion contiene un limite de palabras, en la interfaz existe un textView donde el usuario puede ver la cantidad de caracteres que tiene disponible
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
            // Borrar el evento significa establecer sus campos a null
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
            StyleableToast.makeText(this, "Se ha borrado " + eventoBinding.titulo.text.toString() + " el evento correctamente", Toast.LENGTH_LONG, R.style.toast_by).show()
        }

        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        titulo.text = "Eliminar evento "
        mensaje.text = "¿Quieres borrar el evento actual?"

        dialog.show()
    }

    private fun cargarEvento(evento: Evento?) {
        // Cargamos en el layout los atributos del Evento
        if (evento != null) {
            eventoBinding.titulo.setText(evento.titulo)
            eventoBinding.descripcion.setText(evento.descripcion)
            eventoBinding.ubicacion.setText(evento.ubicacion)
            eventoBinding.textHoraFechaInicio.text = evento.horaInicio
            eventoBinding.textHoraFechaFin.text = evento.horaFin
        }
    }


    private fun actualizarEvento(dao: BetterYouDao, notaId: Int, evento: Evento?) {
        // Cogemos los valores de los campos
        val titulo: String = eventoBinding.titulo.text.toString()
        val ubicacion: String = eventoBinding.ubicacion.text.toString()
        val horaInicio: String = eventoBinding.textHoraFechaInicio.text.toString()
        val horaFin: String = eventoBinding.textHoraFechaFin.text.toString()
        val descripcion: String = eventoBinding.descripcion.text.toString()
        // Realizamos la actualizacion sobre la BBDD
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

    // Establece el la hora seleccionada como texto en la interfaz
    private fun onTimeSelected(time: String, textView: TextView) {
        textView.text = "$time"
    }

    private fun actualizarContadorPalabras() {
        val longitudMaxima = 200

        eventoBinding.descripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(texto: Editable?) {
                // Obtenemos la longitud actual de la descripción aun si 'texto' es null utilizando '?'
                val longitudTexto = texto?.length ?: 0
                eventoBinding.contadorPalabras.text = "$longitudTexto / $longitudMaxima"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // NO APLICA
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // NO APLICA
            }
        })
    }
}