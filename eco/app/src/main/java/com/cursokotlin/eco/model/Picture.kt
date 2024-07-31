package com.cursokotlin.eco.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "pictures",
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
@Parcelize
data class Picture(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val albumId: Int,
    val pictureTitle: String,
    val path: String

): Parcelable
