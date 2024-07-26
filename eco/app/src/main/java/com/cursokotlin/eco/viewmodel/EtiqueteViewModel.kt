package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.eco.model.Etiquete
import com.cursokotlin.eco.repository.EtiquetesRepository
import kotlinx.coroutines.launch

class EtiqueteViewModel(app: Application, private val etiquetesRepository: EtiquetesRepository): AndroidViewModel(app) {

    fun addEtiquete(etiquete: Etiquete)=
        viewModelScope.launch {
            etiquetesRepository.insertEtiquete(etiquete)
        }

    fun updateEtiquete(etiquete: Etiquete)=
        viewModelScope.launch {
            etiquetesRepository.updateEtiquete(etiquete)
        }

    fun deleteEtiquete(etiquete: Etiquete)=
        viewModelScope.launch {
            etiquetesRepository.deleteEtiquete(etiquete)
        }

    fun getAllEtiquetes() = etiquetesRepository.getAllEtiquetes()

}