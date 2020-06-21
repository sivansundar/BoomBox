package com.app.boombox.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "song_table")
@Parcelize
data class Song(
    @PrimaryKey() var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "artist") var artist: String,
    @ColumnInfo(name = "duration") var duration: Long,
    @ColumnInfo(name = "URI") var uri: String,
    @ColumnInfo(name = "album_art") var albumArt: String

) : Parcelable