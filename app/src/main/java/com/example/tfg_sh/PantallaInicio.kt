package com.example.tfg_sh

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.databinding.ActivityPantallaInicioBinding
import com.example.tfg_sh.evento.EventoNotificacionService
import com.example.tfg_sh.toDoList.ItemTarea
import com.example.tfg_sh.toDoList.ObjectTarea
import kotlinx.coroutines.launch


class PantallaInicio : AppCompatActivity() {

    private lateinit var main: ActivityPantallaInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityPantallaInicioBinding.inflate(layoutInflater)
        setContentView(main.root)

        //Instancia BBDD
        val dao = BetterYouBBDD.getInstance(this).betterYouDao

        //Consulta en una Corutina para conseguir todas las Tareas
        lifecycleScope.launch {
            ObjectTarea.listaTareas = dao.getAllTareas() as MutableList<ItemTarea>
        }

        // Creamos el canal de las notificaciones
        crearCanalNotificacion()

        // Al arrancar la app, el handler iniciara el MainActivity después de ese 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 3000)

    }

    private fun crearCanalNotificacion() {
        // Apartir de la version Oreo hacia delante se necesitan crear los canales para las notificaciones
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val canal = NotificationChannel(
                EventoNotificacionService.CANAL_EVENTO_ID,
                "Evento",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            canal.description = "Utilizado para la hora de inicio del evento"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }
}