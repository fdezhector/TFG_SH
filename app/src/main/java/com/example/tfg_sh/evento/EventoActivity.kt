package com.example.tfg_sh.evento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.databinding.ActivityEventoBinding

class EventoActivity : AppCompatActivity() {

    private lateinit var evento: ActivityEventoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        evento = ActivityEventoBinding.inflate(layoutInflater)
        setContentView(evento.root)
    }
}