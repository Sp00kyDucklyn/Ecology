package com.cursokotlin.eco.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cursokotlin.eco.model.Project


@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("SELECT * FROM PROJECTS ORDER BY id")
    fun getAllProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM PROJECTS WHERE projectTitle LIKE :query OR projectDesc LIKE :query")
    fun searchProject(query: String?): LiveData<List<Project>>


    @Query("SELECT projectTitle FROM projects WHERE id = :projectId")
    suspend fun getProjectTitleById(projectId: Int): String?


}