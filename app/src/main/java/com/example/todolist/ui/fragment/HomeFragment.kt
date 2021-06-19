package com.example.todolist.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.todolist.R
import com.example.todolist.adapter.ToDoAdapter
import com.example.todolist.database.ToDoDatabase
import com.example.todolist.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val job = Job()

        val uiScope = CoroutineScope(Dispatchers.Main + job)

        val database = ToDoDatabase.getInstance(requireActivity())

        val todoDao = database.todoDao()

        val toDoAdapter =
            ToDoAdapter(ArrayList(), toDoItemClickListener = ToDoAdapter.ToDoItemClickListener {
                val direction = MainScreenFragmentDirections.actionMainScreenFragmentToEditFragment2()
                requireActivity().findNavController(R.id.fragmentContainerView).navigate(direction)

            }, ToDoAdapter.CheckBoxClickListener {

            })


        binding.recyclerView.apply {
            adapter = toDoAdapter
            setHasFixedSize(true)
        }

        val liveData = todoDao.queryAllToDo()

        liveData.observe(requireActivity(), Observer {
            toDoAdapter.toDoItem = it
            toDoAdapter.notifyDataSetChanged()
        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}