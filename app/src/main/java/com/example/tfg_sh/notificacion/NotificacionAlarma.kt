package com.example.tfg_sh.notificacion

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tfg_sh.MainActivity
import com.example.tfg_sh.PantallaInicio
import com.example.tfg_sh.R

class NotificacionAlarma : BroadcastReceiver() {
    //ID de la notificacion
    companion object {
        const val NOTIFICATION_ID = 1
    }


    //Recibe a traves del intent la informacion para crear la notificacion
    override fun onReceive(context: Context, p1: Intent?) {
        val titulo = p1?.getStringExtra("titulo") ?: "Título de la notificación"
        val textoCorto = p1?.getStringExtra("textoCorto") ?: "Texto corto de la notificación"
        val textoDetallado = p1?.getStringExtra("textoDetallado") ?: "Texto detallado de la notificación"
        crearNotificacionSimple(context,titulo,textoCorto,textoDetallado)
    }

    private fun crearNotificacionSimple(context: Context,titulo: String,textoCorto:String,textoDetallado : String) {

        val intent = Intent(
            context,
            MainActivity::class.java
        ).apply { //Esta notificación se irá a la pantalla principal
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  //Esto es para que no se solapen las notificaciones y se creen por detras muchas notifiaciones
        }

        //Esto es necesario debido a que hay que estabelcer un Flag específico apartir API 28
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val esperaIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)
        //Builder para crear la notificación
        val notificacion = NotificationCompat.Builder(context, PantallaInicio.ID_CANAL)
            .setSmallIcon(R.drawable.betteryou_v1)
            .setContentTitle(titulo)
            .setContentText(textoCorto)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(textoDetallado)
            ) //Notificaciones expandibles
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Esto actua solo android 7.1 abajo
            .setContentIntent(esperaIntent)
            .build()
        //Notifica la nueva notificacion creada
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificacion)
    }

}

