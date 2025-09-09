package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Todo
import com.example.todoapp.TodoAdapter

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
        todoAdapter = TodoAdapter(todoList){ todo, position ->
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_edit_todo, null)
            val editTextInput = view.findViewById<EditText>(R.id.edit_todo_input)
            val saveButton = view.findViewById<Button>(R.id.save_button)
            val cancelButton = view.findViewById<Button>(R.id.cancel_button)

            editTextInput.setText(todo.title)

            builder.setView(view)
            val dialog = builder.create()

            saveButton.setOnClickListener {
                val newTitle = editTextInput.text.toString()
                if (newTitle.isNotEmpty()) {
                    todo.title = newTitle
                    todoAdapter.notifyItemChanged(position)
                    dialog.dismiss()
                }
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ ItemTouchHelperCallback 객체를 먼저 정의
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // 드래그 기능 사용 안 함
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // 왼쪽, 오른쪽 스와이프를 허용
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 스와이프가 발생했을 때 호출되는 메서드
                val position = viewHolder.getBindingAdapterPosition()
                // 할 일 리스트에서 해당 항목 삭제
                todoList.removeAt(position)
                // 어댑터에 항목이 삭제되었음을 알려 UI 업데이트
                todoAdapter.notifyItemRemoved(position)
            }
        }

        // ✅ 정의된 callback을 사용하여 ItemTouchHelper 연결
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

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