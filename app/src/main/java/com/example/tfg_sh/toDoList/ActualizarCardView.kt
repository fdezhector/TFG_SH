package com.example.tfg_sh.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import com.example.tfg_sh.R
import kotlinx.android.synthetic.main.activity_actualizar_card_view.create_priority
import kotlinx.android.synthetic.main.activity_actualizar_card_view.create_title
import kotlinx.android.synthetic.main.activity_actualizar_card_view.delete_button
import kotlinx.android.synthetic.main.activity_actualizar_card_view.update_button

class ActualizarCardView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_card_view)
        val pos = intent.getIntExtra("id",-1)

        if(pos!=1){
            val titulo = DataObject.getDatos(pos).titulo
            val prioridad = DataObject.getDatos(pos).prioridad
            create_title.setText(titulo)
            create_priority.setText(prioridad)

            delete_button.setOnClickListener {
                DataObject.borrarUno(pos)
                volverToDoListMain()
            }

            update_button.setOnClickListener {
                DataObject.actualizarDatos(pos,titulo,prioridad)
                volverToDoListMain()
            }

        }

    }

    fun volverToDoListMain(){
        val intent = Intent(this,ToDoListMain::class.java)
        startActivity(intent)
    }
}