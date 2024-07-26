package com.cursokotlin.eco.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.eco.model.Project
import com.cursokotlin.eco.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(app: Application, private val projectRepository: ProjectRepository): AndroidViewModel(app) {

    fun insertProject(project: Project) =
        viewModelScope.launch {
            projectRepository.insertProject(project)
        }

    fun updateProject(project: Project) =
        viewModelScope.launch {
            projectRepository.updateProject(project)
        }

    fun deleteProject(project: Project) =
        viewModelScope.launch {
            projectRepository.deleteProject(project)
        }

    fun getAllProjects() = projectRepository.getAllProjects()

    fun searchProjects(query: String?) = projectRepository.serchProjects(query)

}