package com.example.todoapp

data class Todo(
    val title: String,
    var isDone: Boolean = false
)