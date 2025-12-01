package com.example.todotest.model
import androidx.lifecycle.*
import com.example.todotest.data.Task
import com.example.todotest.repo.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    val tasks = repo.getAllTasks()

    // Insert or update
    fun insertTask(task: Task, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repo.insert(task)
            onComplete?.invoke()
        }
    }

    fun updateTask(task: Task, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repo.update(task)
            onComplete?.invoke()
        }
    }

    fun deleteTask(task: Task, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repo.delete(task)
            onComplete?.invoke()
        }
    }

    fun getTaskById(id: Int, onResult: (Task?) -> Unit) {
        viewModelScope.launch {
            val t = repo.getById(id)
            onResult(t)
        }
    }
}

class TaskViewModelFactory(private val repo: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

