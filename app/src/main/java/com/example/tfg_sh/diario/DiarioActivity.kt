package com.example.tfg_sh.diario

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.databinding.ActivityDiarioBinding

class DiarioActivity : AppCompatActivity() {

    private lateinit var diario: ActivityDiarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diario = ActivityDiarioBinding.inflate(layoutInflater)
        setContentView(diario.root)
    }

}