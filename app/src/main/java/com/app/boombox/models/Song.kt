package com.app.boombox.models

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
data class Song(
    @PrimaryKey() var id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "artist") var artist : String,
    @ColumnInfo(name = "duration") var duration : Long,
    @ColumnInfo(name = "URI") var uri : String

) {


}