package com.example.tfg_sh.toDoList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfg_sh.R
import kotlinx.android.synthetic.main.activity_to_do_list_main.add
import kotlinx.android.synthetic.main.activity_to_do_list_main.deleteAll
import kotlinx.android.synthetic.main.activity_to_do_list_main.recycler_view

class ToDoListMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list_main)
        add.setOnClickListener {
            val intent = Intent(this,CrearCardView::class.java)
            startActivity(intent)
        }
        deleteAll.setOnClickListener {
            DataObject.borrarTodos()
        }
        recycler_view.adapter=Adaptador(DataObject.getTodosLosDatos())
        recycler_view.layoutManager=LinearLayoutManager(this)
    }
}