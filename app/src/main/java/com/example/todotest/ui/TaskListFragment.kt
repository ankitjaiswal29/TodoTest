package com.example.todotest.ui
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotest.R
import com.example.todotest.adapter.TaskAdapter
import com.example.todotest.data.AppDatabase
import com.example.todotest.databinding.FragmentTaskListBinding
import com.example.todotest.model.TaskViewModel
import com.example.todotest.model.TaskViewModelFactory
import com.example.todotest.repo.TaskRepository
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: TaskViewModel
    private lateinit var adapter: TaskAdapter

    companion object {
        fun newInstance() = TaskListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.getInstance(requireContext()).taskDao()
        val repo = TaskRepository(dao)
        val factory = TaskViewModelFactory(repo)
        vm = ViewModelProvider(requireActivity(), factory).get(TaskViewModel::class.java)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = TaskAdapter(onChecked = { task ->
            vm.updateTask(task)
        }, onItemClick = { task ->
            // Open add/edit fragment with task id
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddEditTaskFragment.newInstance(task.id))
                .addToBackStack(null)
                .commit()
        })

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter

        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddEditTaskFragment.newInstance(null))
                .addToBackStack(null)
                .commit()
        }

        vm.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

