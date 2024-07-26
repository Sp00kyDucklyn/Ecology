package com.cursokotlin.eco.repository

import com.cursokotlin.eco.database.DataBase
import com.cursokotlin.eco.model.Etiquete

class EtiquetesRepository(private val db: DataBase) {

    suspend fun insertEtiquete(etiquete: Etiquete) = db.getEtiqueteDao().insertEtiquete(etiquete)
    suspend fun updateEtiquete(etiquete: Etiquete) = db.getEtiqueteDao().updateEtiquete(etiquete)
    suspend fun deleteEtiquete(etiquete: Etiquete) = db.getEtiqueteDao().deleteEtiquete(etiquete)

    fun getAllEtiquetes() = db.getEtiqueteDao().getAllEtiquete()

}