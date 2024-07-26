package com.cursokotlin.eco.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "albumes")
@Parcelize
data class Album (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val albumTitle: String,
    val albumCoords: String

): Parcelable