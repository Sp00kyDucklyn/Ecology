package com.cursokotlin.eco.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "etiquetes")
@Parcelize
data class Etiquete (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val etiquetenName: String

): Parcelable