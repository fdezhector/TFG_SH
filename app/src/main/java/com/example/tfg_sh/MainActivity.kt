package com.example.tfg_sh

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.bbdd.entidades.Nota
import com.example.tfg_sh.databinding.ActivityMainBinding
import com.example.tfg_sh.diario.DiarioActivity
import com.example.tfg_sh.evento.EventoActivity
import com.example.tfg_sh.notificacion.NotificacionAlarma
import com.example.tfg_sh.toDoList.ToDoListMain
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var main: ActivityMainBinding

    companion object {
        //Id  canal inmutable
        const val ID_CANAL = "canalID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        //Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao
        setContentView(main.root)
        //Canal de la notificacion
        crearCanal()

        cargarVistaVacia()

        main.calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(calendario: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                // cargamos el layout
                cargarVistaElementos()
                // comprobamos si existe o no una nota
                val fechaNota = Utils.obtenerFechaNota(year, month, dayOfMonth)
                val id = Utils.setId(fechaNota)
                lifecycleScope.launch {
                    var nota: Nota?
                    nota = existeNota(dao, id)
                    //Si no existe, creamos todas las entidades
                    if (nota == null) {
                        insertarTodasEntidades(nota, id, fechaNota, dao)
                    }
                }
            }
        })

        main.vistaEvento.setOnClickListener {
            val vistaEventoActivty = Intent(applicationContext, EventoActivity::class.java)
            startActivity(vistaEventoActivty)
        }

        main.vistaDiario.setOnClickListener {
            val vistaDiarioActivity = Intent(applicationContext, DiarioActivity::class.java)
            startActivity(vistaDiarioActivity)
        }

        main.vistaToDoList.setOnClickListener {
            scheduleNotificacion()
            val vistaToDoListActivity = Intent(applicationContext, ToDoListMain::class.java)
            startActivity(vistaToDoListActivity)
        }

    }

    private suspend fun insertarTodasEntidades(
        nota: Nota?, id: Int, fechaNota: Date, dao: BetterYouDao
    ) {
        var nota1 = nota
        nota1 = Nota(id, fechaNota.toString())
        dao.insertNota(nota1)
        dao.insertDiario(
            Diario(
                etiquetas = null, descripcion = null, notaId = nota1.id
            )
        )
        dao.insertEvento(
            Evento(
                titulo = null,
                descripcion = null,
                ubicacion = null,
                horaInicio = null,
                horaFin = null,
                notaId = nota1.id
            )
        )
    }

    //Esto es para crear el canal de la notificacion
    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ID_CANAL, "MySuperChannel", NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "canal de nuestra notificación"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    //Esto nos sirve para ponerle un timer a la app y que la notificacion salga mas tarde
    private fun scheduleNotificacion() {
        val intent = Intent(applicationContext, NotificacionAlarma::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NotificacionAlarma.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + 10000, pendingIntent
        ) //3 parametro cuando va a salir la notificacion
    }

    // FIXME mirar si se puede hacer estos dos métodos en uno solo
    private fun cargarVistaVacia() {
        main.layoutElementos.visibility = View.GONE
        main.layoutLogo.visibility = View.VISIBLE
    }

    private fun cargarVistaElementos(){
        if(main.layoutLogo.visibility == View.VISIBLE){
            main.layoutLogo.visibility = View.GONE
            main.layoutElementos.visibility = View.VISIBLE
        }
    }

    private suspend fun existeNota(dao: BetterYouDao, id: Int): Nota? {
        return dao.getNota(id)
    }
}


