package com.example.tfg_sh.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tfg_sh.MainActivity

class Notificacion  //Constructor tenemos que decirle a donde quiere ir, titulo y mensaje (Parametrizar)
    : AppCompatActivity() {
    private var pendingIntent: PendingIntent? = null
    fun crearNotificacion() {
        //Comprobamos el build para que sea superior 8.0 o 0 oreo debido a que se maneja por canales
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mostrarNotificacion()
        } else {
            mostrarNuevaNotificacion()
        }
    }

    fun mostrarNotificacion() {
        val canal = NotificationChannel(ID_CANAL, "Nuevo", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
        mostrarNuevaNotificacion()
    }

    fun mostrarNuevaNotificacion() {
        //Mirar con hector l45
        direccionarPantalla(MainActivity::class.java)
        val builder = NotificationCompat.Builder(applicationContext, ID_CANAL)
            .setContentTitle("Mi primera notificacion")
            .setContentText("Esta es una prueba de notificacion")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        //.setSmallIcon() Establecer un icono para la notificacion R.drawable.(imagen)
        val managerCompat = NotificationManagerCompat.from(applicationContext)
        //managerCompat.notify(1, builder.build())
    }

    //Este método nos va a permitir redirigir la pantalla a donde nosotros queramos pasándolo por parametro
    fun direccionarPantalla(classActivity: Class<*>?) {
        val intent = Intent(this, classActivity)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(classActivity)
        stackBuilder.addNextIntent(intent)
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        //Id y nombre canal inmutable
        private const val ID_CANAL = "canalID"
        private const val NOMBRE_CANAL = "canalNombre"
    }
}