package com.example.tfg_sh.notificacion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.tfg_sh.evento.EventoNotificacionService

class NotificacionAlarma : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val eventoNotificacionService = EventoNotificacionService(context)

        val titulo = intent?.getStringExtra("titulo")
        val texto = intent?.getStringExtra("texto")

        eventoNotificacionService.mostrarNotificacion(titulo!!, texto!!)
    }
}

