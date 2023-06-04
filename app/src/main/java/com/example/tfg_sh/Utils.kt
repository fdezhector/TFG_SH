package com.example.tfg_sh

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.example.tfg_sh.notificacion.NotificacionAlarma
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Utils {

    // Creamos la lista inmutable de prioridades que se utilizara para las tareas
    val prioridades = listOf("Alta", "Media", "Baja")

    fun goToMainScreen(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        // FLAG_ACTIVITY_CLEAR_TOP permite eliminar todas las actividades que esten por encima de ella en la cola
        // FLAG_ACTIVITY_SINGLE_TOP permite reutilizar la instancia existente de la activadad a la que se quiere llegar en lugar de crear una nueva
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }

    fun goToSettings(activity: Activity) {
        val intent = Intent(activity, Settings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }

    fun setId(fecha_nota: Date): Int {
        // el id será la fecha del día de la nota
        // por ejemplo: 25 de abril de 2023 --> id = 25042023
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val fecha = sdf.format(fecha_nota)
        return fecha.replace("/".toRegex(), "").toInt()
    }

    fun obtenerFechaNota(year: Int, month: Int, dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }

    fun obtenerHora(horaSinFormatear: String): Date {
        val format = SimpleDateFormat("HH:mm")
        val hora: Date = format.parse(horaSinFormatear)
        return hora
    }

    fun initDropDownMenu(dropDown: Spinner, context: Context) {
        // creamos el adaptador para el spinner
        val adaptador = ArrayAdapter(context, R.layout.dropdown_menu, prioridades)
        // definimos el estilo de los desplegables
        adaptador.setDropDownViewResource(R.layout.dropdown_menu_item)

        // relacionamos el adaptador con el spinner del xml
        dropDown.adapter = adaptador

        // listener que se ejectutara cuando se seleccione una prioridad
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position) as String

                val itemMostrado = (context as Activity).findViewById<CheckedTextView>(R.id.dropdown_menu_layout)
                // evaluamos que color debera tener el fondo del item cuando seleccionemos una prioridad
                itemMostrado.setBackgroundColor(evaluarColor(selectedItem, context))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // NO APLICA | Aqui se manejaria el caso en el que no se haya seleccionado ninguna opción
            }
        }
    }

    fun programarNotificacion(
        context: Context,
        titulo: String,
        textoCorto: String,
        textoDetallado: String,
        delay: Int
    ) {
        val intent = Intent(context, NotificacionAlarma::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("textoCorto", textoCorto)
            putExtra("textoDetallado", textoDetallado)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NotificacionAlarma.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + delay, pendingIntent
        )
    }

    /**
     * Nos permite crear el canal de la notificación
     */
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PantallaInicio.ID_CANAL, "CanalBetterYou", NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para la notificacion de la aplicacion BetterYou"
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun evaluarColor(selectedItem: String, context: Context): Int {
        // Depenediendo de la prioridad, pondremos un color de fondo diferente al elemnto
        val dropDownColor: Int =
        if (selectedItem.equals("Alta", ignoreCase = true)) {
            ContextCompat.getColor(context, R.color.prioridad_alta_1)
        } else if (selectedItem.equals("Media", ignoreCase = true)) {
            ContextCompat.getColor(context, R.color.prioridad_media_1)
        } else {
            ContextCompat.getColor(context, R.color.colorContainer)
        }
        return dropDownColor
    }

    fun isPermisoOtorgado(context: Context, permission: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

}


