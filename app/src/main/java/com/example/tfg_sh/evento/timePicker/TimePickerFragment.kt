package com.example.tfg_sh.evento.timePicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.tfg_sh.R
import java.util.Calendar

class TimePickerFragment(val listener:(String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val formattedHour = String.format("%02d", hourOfDay)
        val formattedMinute = String.format("%02d", minute)
        listener("$formattedHour:$formattedMinute")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minutos = calendario.get(Calendar.MINUTE)
        return TimePickerDialog(activity as Context, R.style.TimePicker, this, hora, minutos, true)
    }
}