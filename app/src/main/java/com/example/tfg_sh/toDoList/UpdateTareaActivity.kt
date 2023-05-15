package com.example.tfg_sh.toDoList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.bbdd.BetterYouBBDD
import com.example.tfg_sh.databinding.ActivityUpdateTareaBinding

class UpdateTareaActivity : AppCompatActivity() {

    private lateinit var updateTarea: ActivityUpdateTareaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTarea = ActivityUpdateTareaBinding.inflate(layoutInflater)
        setContentView(updateTarea.root)

        val dao = BetterYouBBDD.getInstance(this).betterYouDao

    }
}