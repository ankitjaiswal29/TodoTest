package com.example.todotest.ui
import com.example.todotest.R
import com.example.todotest.data.AppDatabase
import com.example.todotest.data.Task
import com.example.todotest.databinding.FragmentAddEditTaskBinding
import com.example.todotest.model.TaskViewModel
import com.example.todotest.model.TaskViewModelFactory
import com.example.todotest.repo.TaskRepository
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.Date

class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: TaskViewModel
    private var currentTask: Task? = null
    private var taskId: Int? = null

    companion object {
        private const val ARG_TASK_ID = "arg_task_id"
        fun newInstance(id: Int?) = AddEditTaskFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_TASK_ID, id) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        taskId = arguments?.getSerializable(ARG_TASK_ID) as? Int

        val dao = AppDatabase.getInstance(requireContext()).taskDao()
        val repo = TaskRepository(dao)
        val factory = TaskViewModelFactory(repo)
        vm = ViewModelProvider(requireActivity(), factory).get(TaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = binding.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        if (taskId != null) {
            vm.getTaskById(taskId!!) { t ->
                activity?.runOnUiThread {
                    t?.let { populateFields(it) }
                }
            }
        }else {
            currentTask = null
            //val dt = currentTask?.dateTime ?: Date()
            //binding.tvDateTime.text=dt.format()
            activity?.invalidateOptionsMenu()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subtitle = binding.etSubtitle.text.toString().trim()
            //val dt = currentTask?.dateTime // may be set by pickDateTime or left null
            //val dt = currentTask?.dateTime ?: Date()
            if (currentTask == null) {
                //val newTask = Task(title = title, subtitle = if (subtitle.isEmpty()) null else subtitle, dateTime = dt)
                val newTask = Task(title = title)
                vm.insertTask(newTask) {
                    // pop back
                    parentFragmentManager.popBackStack()
                }
            } else {
                currentTask!!.title = title
                //currentTask!!.subtitle = if (subtitle.isEmpty()) null else subtitle
                //currentTask!!.dateTime = dt
                vm.updateTask(currentTask!!) {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun populateFields(task: Task) {
        currentTask = task
        binding.etTitle.setText(task.title)
        //binding.etSubtitle.setText(task.subtitle ?: "")
//        if (task.dateTime != null) {
//            //binding.tvDateTime.text = task.dateTime.toString()
//            binding.tvDateTime.text = task.dateTime?.format() ?: "No date"
//
//        }else{
//            binding.tvDateTime.text = task.dateTime?.format() ?: "No date"
//        }
        activity?.invalidateOptionsMenu()
    }
    fun Date.format(): String {
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a")
        return sdf.format(this)
    }


    // delete menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        val deleteItem = menu.findItem(R.id.action_delete)
        deleteItem.isVisible = currentTask != null
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            currentTask?.let { task ->
                vm.deleteTask(task) {
                    parentFragmentManager.popBackStack()
                }
            } ?: run {

                Toast.makeText(requireContext(), "No task to delete", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

