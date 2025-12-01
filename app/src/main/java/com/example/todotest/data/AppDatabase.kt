package com.example.todotest.data
import android.content.Context
import androidx.room.*
@Database(entities = [Task::class], version = 1, exportSchema = false)
//@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_db"
                ).build()
                INSTANCE = inst
                inst
            }
        }
    }
}
