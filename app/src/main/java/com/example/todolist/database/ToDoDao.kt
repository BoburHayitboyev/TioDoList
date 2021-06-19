package com.example.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.Item

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo")
    fun queryAllToDo(): LiveData<List<Item>>

    @Insert
    fun insertToDO(item: Item)

    @Delete
    fun deleteToDo(item: Item)

    @Update
    fun updateToDO(item: Item)
}