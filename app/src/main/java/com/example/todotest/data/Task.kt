package com.example.todotest.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var subtitle: String? = null,
    var dateTime: Date? = null,
    var isCompleted: Boolean = false
)

