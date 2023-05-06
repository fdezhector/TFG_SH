package com.example.tfg_sh.toDoList

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "To_Do")
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var titulo:String,
    var prioridad:String
)