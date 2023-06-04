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
import com.example.tfg_sh.toDoList.ItemTarea

/**
 * Esta es la interfaz de Room que va a realizar las consultas a la BBDD
 */
@Dao
interface BetterYouDao {
    //NOTA
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotas(notas: List<Nota>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNota(nota: Nota)

    @MainThread
    @Query("select * from nota where id = :id")
    suspend fun getNota(id: Int): Nota?

    @Query("select * from nota")
    suspend fun getAllNotas(): List<Nota>

    //EVENTO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEventos(eventos: List<Evento>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvento(evento: Evento)

    @Update
    suspend fun updateEvento(evento: Evento)

    @Query("select * from evento where id = :id")
    suspend fun getEvento(id: Int): Evento

    @Query("select * from evento")
    suspend fun getAllEventos(): List<Evento>

    @Query("select * from evento where notaId = :notaId")
    suspend fun getEventoNotaId(notaId: Int): Evento


    //DIARIO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDiarios(diario: List<Diario>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiario(diario: Diario)

    @Update
    suspend fun updateDiario(diario: Diario)

    @Query("select * from diario where id = :id")
    suspend fun getDiario(id: Int): Diario

    @Query("select * from diario")
    suspend fun getAllDiarios(): List<Diario>

    @Query("select * from diario where notaId = :notaId")
    suspend fun getDiarioNotaId(notaId: Int): Diario


    //TAREA
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTareas(tarea: List<Tarea>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTarea(tarea: Tarea)

    @Update
    suspend fun updateTarea(tarea: Tarea)

    @Delete
    suspend fun deleteTarea(tarea: Tarea)

    @Query("delete from tarea")
    suspend fun deleteAllTareas()

    @Query("select * from tarea where id = :id")
    suspend fun getTarea(id: Int): Tarea

    @Query("select * from tarea")
    suspend fun getAllTareas(): List<ItemTarea>
}