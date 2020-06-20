package com.app.boombox.database

import android.content.Context
import android.provider.MediaStore
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.boombox.Repository.SongRepository
import com.app.boombox.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class BoomboxDatabase : RoomDatabase(){

    abstract fun songDao() : SongDAO



    private class BoomboxDatabaseCallback(
        private val scope : CoroutineScope,
        private val context: Context

    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE.let { boomboxDatabase ->
                scope.launch(Dispatchers.IO) {
                    Timber.i("BoomBox Database onCreate coroutine")

                }
            }

        }

    }

    companion object {
        @Volatile
        private var INSTANCE : BoomboxDatabase? = null

        fun getDatabase(context : Context,
                        scope : CoroutineScope) : BoomboxDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoomboxDatabase::class.java,
                    "boombox_database").
                     addCallback(BoomboxDatabaseCallback(scope, context))
                    .build()



                    INSTANCE = instance
                    return instance
            }
        }
    }


}