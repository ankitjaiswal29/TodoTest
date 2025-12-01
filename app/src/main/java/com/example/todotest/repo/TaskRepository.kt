package com.example.todotest.repo
import androidx.lifecycle.LiveData
import com.example.todotest.data.Task
import com.example.todotest.data.TaskDao

class TaskRepository(private val dao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = dao.getAllTasks()

    suspend fun insert(task: Task): Long = dao.insert(task)

    suspend fun update(task: Task) = dao.update(task)

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun getById(id: Int): Task? = dao.getTaskById(id)
}

