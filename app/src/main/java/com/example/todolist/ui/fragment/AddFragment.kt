package com.example.todolist.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.navigation.fragment.findNavController
import com.example.todolist.database.ToDoDatabase
import com.example.todolist.databinding.FragmentAddBinding
import com.example.todolist.model.Item
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var cal = Calendar.getInstance()
    private var _binding: FragmentAddBinding? = null
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
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val job = Job()

        val uiScope = CoroutineScope(Dispatchers.Main + job)

        val database = ToDoDatabase.getInstance(requireActivity())

        val todoDao = database.todoDao()

        binding.dateEdt.setOnClickListener {
            getDate(binding.dateEdt)
        }

        binding.timeEdt.setOnClickListener {
            getTime(binding.timeEdt)
        }

        binding.createBtn.setOnClickListener {
            if (editTextsIsEmpty()) {
                val todo = Item(
                    title = binding.taskTitleEdt.text.toString(),
                    date = binding.dateEdt.text.toString(),
                    time = binding.timeEdt.text.toString(),
                    check = false
                )

                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        todoDao.insertToDO(todo)
                    }
                }
                findNavController().popBackStack()
            } else {
                Snackbar.make(
                    binding.constraintLayout,
                    "Ma'lumotlarni to'liq kiritingda oka.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun getTime(editText: EditText) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            var trueHour = hour.toString()
            var trueMinute = minute.toString()

            if (hour < 10) {
                trueHour = formatCorrection(hour)
            }
            if (minute < 10) {
                trueMinute = formatCorrection(minute)
            }
            editText.setText("$trueHour:$trueMinute")
        }

        TimePickerDialog(
            requireActivity(),
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun getDate(editText: EditText) {

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog(
            requireActivity(), { view, year, month, dayOfMonth ->
                var trueMonth = month.toString()
                var trueDay = dayOfMonth.toString()

                if (month + 1 < 10) {
                    trueMonth = formatCorrection(month + 1)
                }
                if (dayOfMonth < 10) {
                    trueDay = formatCorrection(dayOfMonth)
                }

                editText.setText("$year:$trueMonth:$trueDay")
            }, year, month, day
        )

        dateSetListener.show()
    }

    //date va timega 1-10 oralig'idagi sonlarga 0 qo'shish
    private fun formatCorrection(int: Int): String {
        return "0$int"
    }

    private fun editTextsIsEmpty(): Boolean {
        if (binding.taskTitleEdt.text.isEmpty() || binding.timeEdt.text.isEmpty() || binding.dateEdt.text.isEmpty()) {
            return false
        }
        return true
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}