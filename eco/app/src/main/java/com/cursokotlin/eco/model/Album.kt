package com.cursokotlin.eco.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "albumes",
            foreignKeys = [
                ForeignKey(
                    entity = Project::class,
                    parentColumns = ["id"],
                    childColumns = ["projectId"],
                    onDelete = ForeignKey.CASCADE
                )
            ]
            )
@Parcelize
data class Album (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val projectId: Int,
    val albumTitle: String,
    val albumCoords: String
): Parcelable