package com.example.tfg_sh.evento

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tfg_sh.MainActivity
import com.example.tfg_sh.R
import com.example.tfg_sh.notificacion.NotificacionAlarma
import java.util.Calendar

class EventoNotificacionService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun mostrarNotificacion(titulo: String, texto: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_EVENTO_ID)
            .setSmallIcon(R.drawable.betteryou_logo)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notificacion)
    }

    fun programarNotificacion(anno: Int, mes: Int, dia: Int, hora: Int, minuto: Int, titulo: String, texto: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificacionAlarma::class.java)
        intent.apply {
            putExtra("titulo", titulo)
            putExtra("texto", texto)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, anno)
        calendar.set(Calendar.MONTH, mes - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dia)
        calendar.set(Calendar.HOUR_OF_DAY, hora)
        calendar.set(Calendar.MINUTE, minuto)
        calendar.set(Calendar.SECOND, 0)

        val delay = 15 * 60 * 1000 // la notificacion saldra 15 min antes

        // Programar la notificación en el momento deseado
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis - delay,
            pendingIntent
        )
    }

    companion object {
        const val CANAL_EVENTO_ID = "canal_evento"
    }
}