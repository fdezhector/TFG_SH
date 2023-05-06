package com.example.tfg_sh.toDoList

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAOToDoList {
    @Insert
    suspend fun insertTarea(entity: ToDoList)

    @Update
    suspend fun updateTarea(entity: ToDoList)

    @Delete
    suspend fun deleteTarea(entity: ToDoList)

    @Query("delete from To_Do")
    suspend fun borrarTodasTareas()

    @Query("Select * from To_Do")
    suspend fun getTareas(): List<CardViewInfo>
}