package com.example.tfg_sh

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.bbdd.dao.BetterYouDao
import com.example.tfg_sh.bbdd.entidades.DatosBBDD
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.bbdd.entidades.Nota
import com.example.tfg_sh.bbdd.entidades.Tarea
import com.example.tfg_sh.databinding.ActivityMainBinding
import com.example.tfg_sh.diario.DiarioActivity
import com.example.tfg_sh.evento.EventoActivity
import com.example.tfg_sh.notificacion.NotificacionAlarma
import com.example.tfg_sh.toDoList.ToDoListMain
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File
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
                    val nota: Nota?
                    nota = existeNota(dao, id)
                    //Si no existe, creamos todas las entidades
                    if (nota == null) {
                        insertarTodasEntidades(nota, id, fechaNota, dao)
                    }
                }
            }
        })

        main.vistaEvento.setOnClickListener {
            //exportarBBDD(dao,this)
            //importarBBDD(dao,this)
            val vistaEventoActivty = Intent(applicationContext, EventoActivity::class.java)
            startActivity(vistaEventoActivty)
        }

        main.vistaDiario.setOnClickListener {
            val vistaDiarioActivity = Intent(applicationContext, DiarioActivity::class.java)
            startActivity(vistaDiarioActivity)
        }

        main.vistaToDoList.setOnClickListener {
            val titulo = "Notificacion de prueba"
            val textoCorto = "Tienes que ingresar a la app para realizar tus tareas"
            val textoDetallado = "Tienes pendientes muchas tareas, deberias ir realizandolas o se te van a acumular"
            scheduleNotificacion(titulo,textoCorto,textoDetallado)
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
                ID_CANAL, "CanalDeNotificacion", NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "canal de nuestra notificación"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //Esto nos sirve para ponerle un timer a la app y que la notificacion salga mas tarde
    private fun scheduleNotificacion(titulo: String,textoCorto:String,textoDetallado : String) {
        val intent = Intent(applicationContext, NotificacionAlarma::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("textoCorto",textoCorto )
            putExtra("textoDetallado", textoDetallado)
        }
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
            val animation = AnimationUtils.loadAnimation(this, R.anim.anim_elementos)
            main.layoutElementos.startAnimation(animation)
        }
    }

    private suspend fun existeNota(dao: BetterYouDao, id: Int): Nota? {
        return dao.getNota(id)
    }

    private fun exportarBBDD(dao: BetterYouDao,context:Context){
        Log.e("FILEDIR", getExternalFilesDir(null).toString())
        //Voy creando las listas de todas las entidades de la BBDD
        lifecycleScope.launch{
            var diarios = dao.getAllDiarios()
            var eventos = dao.getAllEventos()
            var tareas = dao.getAllTareas() as List<Tarea>
            var notas = dao.getAllNotas()

            var datos = DatosBBDD(diarios,eventos,notas,tareas)
            //Parseo JSON
            val gson = Gson()
            val json = gson.toJson(datos)
            //Creamos el fichero
            val file = File(context.getExternalFilesDir(null), "data.json")
            file.writeText(json)
        }
    }
    //FIXME Importar y exportar funcionan solo que cuando importamos tenemos que solucionar que cuando esta cargada la aplicacion no se aplica al recycler view de manera que tenemos que reiniciar para que se carguen los datos de la bbdd
    private fun importarBBDD(dao: BetterYouDao,context:Context) {
        try {
            //Obtén la referencia al archivo JSON en el almacenamiento externo.
            val file = File(context.getExternalFilesDir(null), "data.json")
            //Leo el archivo
            val jsonString = file.readText()
            //Convierto el archivo en objetos de tipo DatosBBDD
            val gson = Gson()
            val datos = gson.fromJson(jsonString, DatosBBDD::class.java)
            //Insertamos todos los datos en la BBDD
            lifecycleScope.launch {
                dao.insertAllDiarios(datos.diarios)
                dao.insertAllEventos(datos.eventos)
                dao.insertAllTareas(datos.tareas)
                dao.insertAllNotas(datos.notas)
            }
        } catch (e: Exception) {
            Log.e("ImportarBBDD", "Error al importar los datos desde el archivo JSON", e)
        }
    }
}


