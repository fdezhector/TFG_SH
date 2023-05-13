package com.example.tfg_sh

import android.app.Activity
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Utils {
    fun goToPreviousScreen(activity: Activity){
        activity.finish()
    }
    fun goToMainScreen(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
    }

    fun setId(fecha_nota: Date?): Int {
        // el id será la fecha del día de la nota
        // por ejemplo: 5 de abril de 2023 --> id = 5042023
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val fecha = sdf.format(fecha_nota)
        return fecha.replace("/".toRegex(), "").toInt()
    }

    fun obtenerFechaNota(year: Int, month: Int, dayOfMonth: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar.time
    }
}