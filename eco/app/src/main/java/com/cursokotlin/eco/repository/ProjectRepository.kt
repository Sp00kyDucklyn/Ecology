package com.cursokotlin.eco.repository

import com.cursokotlin.eco.database.DataBase
import com.cursokotlin.eco.model.Project

class ProjectRepository(private val db: DataBase) {

    suspend fun insertProject(project: Project) = db.getProjectDao().insertProject(project)
    suspend fun updateProject(project: Project) = db.getProjectDao().updateProject(project)
    suspend fun deleteProject(project: Project) = db.getProjectDao().deleteProject(project)

    fun getAllProjects() = db.getProjectDao().getAllProjects()
    fun serchProjects(query: String?) = db.getProjectDao().searchProject(query)
    suspend fun getProjectTitleById(projectId: Int) = db.getProjectDao().getProjectTitleById(projectId)

}