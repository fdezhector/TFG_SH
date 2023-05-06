package com.example.tfg_sh.toDoList

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoList::class],version=1)
abstract class BBDDToDoList : RoomDatabase() {
    abstract fun dao():DAOToDoList
}