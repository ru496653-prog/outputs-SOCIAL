package com.example.outputs.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        EvidenceEntity::class,
        TheoryEntity::class,
        TimelineEntity::class,
        GroupEntity::class,
        MessageEntity::class,
        UserSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OutputsDatabase : RoomDatabase() {
    abstract fun outputsDao(): OutputsDao

    companion object {
        @Volatile
        private var INSTANCE: OutputsDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): OutputsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OutputsDatabase::class.java,
                    "outputs_database.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(OutputsDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class OutputsDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        InitialData.populateDatabase(database.outputsDao())
                    }
                }
            }
        }
    }
}
