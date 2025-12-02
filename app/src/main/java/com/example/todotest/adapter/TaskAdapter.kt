package com.example.todotest.adapter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todotest.data.Task
import com.example.todotest.databinding.ItemTaskBinding
import com.example.todotest.utils.format

class TaskAdapter(
    private var items: List<Task> = emptyList(),
    private val onChecked: (Task) -> Unit,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    fun submitList(newItems: List<Task>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.tvTitle.text = task.title
            binding.tvSubtitle.text = task.subtitle ?: ""
            binding.tvDate.text= task.dateTime?.format() ?: "No date"
            binding.chkDone.setOnCheckedChangeListener(null)
            if (task.isCompleted) {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.chkDone.isChecked = true
            } else {
                binding.tvTitle.paintFlags = binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.chkDone.isChecked = false
            }

            binding.chkDone.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                onChecked(task)
            }

            binding.root.setOnClickListener {
                onItemClick(task)
            }
        }
    }
}

