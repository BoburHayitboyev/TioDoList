package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TodoItemBinding
import com.example.todolist.model.Item

class ToDoAdapter(
    var toDoItem: List<Item>,
    val toDoItemClickListener: ToDoItemClickListener,
    val checkBoxClickListener: CheckBoxClickListener
) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.timeTv.text = toDoItem[position].time
        holder.binding.todoTitleTv.text = toDoItem[position].title

        holder.binding.checkbox.isChecked = toDoItem[position].check

        holder.binding.root.setOnClickListener {
            toDoItemClickListener.onToDoClick(toDoItem[position])
        }

        holder.binding.checkbox.setOnClickListener {
            checkBoxClickListener.checkBoxClick(toDoItem[position])
        }
    }

    override fun getItemCount() = toDoItem.size

    class ToDoItemClickListener(val toDoItemClickListener: (item: Item) -> Unit) {
        fun onToDoClick(item: Item) = toDoItemClickListener(item)
    }

    class CheckBoxClickListener(val checkBoxClickListener: (item:Item)->Unit){
        fun checkBoxClick(item: Item) = checkBoxClickListener(item)
    }

    inner class ToDoViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

}