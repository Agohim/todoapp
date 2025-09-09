// MainActivity.kt
package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoList: MutableList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 가상 데이터
        todoList = mutableListOf(
            Todo("Android Studio 앱 만들기"),
            Todo("멋진 UI 디자인 적용하기", isDone = true)
        )

        // 레이아웃 요소 찾기
        val recyclerView: RecyclerView = findViewById(R.id.todo_list_recycler_view)
        val todoInput: EditText = findViewById(R.id.todo_input)
        val addButton: ImageButton = findViewById(R.id.add_button)

        // RecyclerView 설정
        todoAdapter = TodoAdapter(todoList) { todo ->
            // 할 일 항목의 완료 상태를 토글
            todo.isDone = !todo.isDone
        }

        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 버튼 클릭 이벤트
        addButton.setOnClickListener {
            val newTodoTitle = todoInput.text.toString()
            if (newTodoTitle.isNotEmpty()) {
                val newTodo = Todo(newTodoTitle)
                todoList.add(newTodo)
                todoAdapter.notifyItemInserted(todoList.size - 1)
                todoInput.text.clear() // 입력창 비우기
            }
        }
    }
}