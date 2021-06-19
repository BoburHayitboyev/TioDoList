package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TodoItemBinding
import com.example.todolist.model.Item

class ToDoAdapter(
    private var item: List<Item>,
    private val toDoItemClickListener: ToDoItemClickListener
) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.timeTv.text = item[position].time
        holder.binding.todoTitleTv.text = item[position].title

        holder.binding.checkbox.isChecked = item[position].check

        holder.binding.root.setOnClickListener {
            toDoItemClickListener.onToDoClick(item[position])
        }

        holder.binding.checkbox.setOnClickListener {

        }
    }

    override fun getItemCount() = item.size

    class ToDoItemClickListener(val toDoItemClickListener: (item: Item) -> Unit) {
        fun onToDoClick(item: Item) = toDoItemClickListener(item)
    }

    inner class ToDoViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

}