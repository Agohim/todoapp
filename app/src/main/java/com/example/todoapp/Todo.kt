package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(true)
    var id: Int= 0,
    var title: String,
    var isDone: Boolean = false
)