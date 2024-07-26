package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cursokotlin.eco.repository.AlbumRepository
import com.cursokotlin.eco.repository.EtiquetesRepository

class EtiqueteViewModelFactory(val app: Application, private val etiquetesRepository: EtiquetesRepository):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EtiqueteViewModel(app, etiquetesRepository) as T
    }

}