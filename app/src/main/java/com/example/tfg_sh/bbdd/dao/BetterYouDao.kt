package com.example.tfg_sh.bbdd.dao

import androidx.annotation.MainThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tfg_sh.bbdd.entidades.Diario
import com.example.tfg_sh.bbdd.entidades.Evento
import com.example.tfg_sh.bbdd.entidades.Nota
import com.example.tfg_sh.bbdd.entidades.Tarea

@Dao
interface BetterYouDao {
    //NOTA
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNota(nota:Nota)
    @MainThread
    @Query("select * from nota where id = :id")
    suspend fun getNota(id:Int):Nota?

    //EVENTO
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvento(evento: Evento)
    @Update
    suspend fun updateEvento(evento: Evento)
    @Delete
    suspend fun deleteEvento(evento: Evento)
    @Query("select * from evento where id = :id")
    suspend fun getEvento(id: Int):Evento

    //DIARIO
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiario(diario: Diario)
    @Update
    suspend fun updateDiario(diario: Diario)
    @Delete
    suspend fun deleteDiario(diario: Diario)
    @Query("select * from diario where id = :id")
    suspend fun getDiario(id: Int):Diario

    //TAREA
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTarea(tarea: Tarea)
    @Update
    suspend fun updateTarea(tarea: Tarea)
    @Delete
    suspend fun deleteTarea(tarea: Tarea)
    @Query("delete from tarea")
    suspend fun deleteAllTareas()
    @Query("select * from tarea")
    suspend fun getAllTareas():List<Tarea>
}