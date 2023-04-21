package com.example.tfg_sh.toDoList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_sh.databinding.ActivityToDoListBinding

class ToDoListActivity : AppCompatActivity() {

    private lateinit var toDoList: ActivityToDoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toDoList = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(toDoList.root)
    }
}