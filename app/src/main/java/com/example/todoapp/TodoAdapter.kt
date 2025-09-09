package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class TodoAdapter(private var todoList: List<Todo>, private val onTodoClick: (Todo) -> Unit) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoTitle: TextView = view.findViewById(R.id.todo_title)
        val completeCheckbox: CheckBox = view.findViewById(R.id.complete_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoTitle.text = todo.title

        holder.completeCheckbox.isChecked = todo.isDone

        // 체크박스 클릭 리스너 설정
        holder.completeCheckbox.setOnClickListener {
            onTodoClick(todo)
        }

        if (todo.isDone) {
            holder.todoTitle.paintFlags = holder.todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.todoTitle.setTextColor(0xFF888888.toInt())
        } else {
            holder.todoTitle.paintFlags = holder.todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.todoTitle.setTextColor(0xFF000000.toInt())
        }
    }

    override fun getItemCount() = todoList.size

    // 이 메서드는 외부에서 데이터를 업데이트할 때 사용합니다.
    fun updateData(newTodoList: List<Todo>) {
        todoList = newTodoList
        notifyDataSetChanged()
    }

    // ✅ public 함수를 추가하여 외부에서 todoList에 안전하게 접근
    fun getTodoAt(position: Int): Todo {
        return todoList[position]
    }
}