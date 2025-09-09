// MainActivity.kt

package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Todo
import com.example.todoapp.TodoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext // withContext를 위한 import
import android.app.AlertDialog
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var database: TodoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = TodoDatabase.getDatabase(this)

        val recyclerView: RecyclerView = findViewById(R.id.todo_list_recycler_view)
        val todoInput: EditText = findViewById(R.id.todo_input)
        val addButton: ImageButton = findViewById(R.id.add_button)

        todoAdapter = TodoAdapter(emptyList()) { todo ->
            CoroutineScope(Dispatchers.IO).launch {
                todo.isDone = !todo.isDone
                database.todoDao().update(todo)
            }
        }
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ 수정된 부분: UI 업데이트 코드를 메인 스레드로 옮깁니다.
        CoroutineScope(Dispatchers.IO).launch {
            database.todoDao().getAll().collect { todos ->
                withContext(Dispatchers.Main) { // ✅ 메인 스레드로 전환
                    todoAdapter.updateData(todos)
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.getBindingAdapterPosition()
                val todoToDelete = todoAdapter.getTodoAt(position)
                CoroutineScope(Dispatchers.IO).launch {
                    database.todoDao().delete(todoToDelete)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        addButton.setOnClickListener {
            val newTodoTitle = todoInput.text.toString()
            if (newTodoTitle.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    database.todoDao().insert(Todo(title = newTodoTitle))
                }
                todoInput.text.clear()
            }
        }
    }
}