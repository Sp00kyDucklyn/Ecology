package com.cursokotlin.eco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cursokotlin.eco.model.Etiquete


@Dao
interface EtiqueteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEtiquete(etiquete: Etiquete)

    @Update
    suspend fun updateEtiquete(etiquete: Etiquete)

    @Delete
    suspend fun deleteEtiquete(etiquete: Etiquete)

    @Query("SELECT * FROM ETIQUETES ORDER BY id")
    fun getAllEtiquete(): LiveData<List<Etiquete>>

}