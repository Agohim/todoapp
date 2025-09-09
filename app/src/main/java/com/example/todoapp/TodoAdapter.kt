package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todoList: MutableList<Todo>, private val onItemLongClick: (Todo, Int) -> Unit) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
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

        //체크박스 상태를 데이터와 동기화
        holder.completeCheckbox.isChecked = todo.isDone

        //항목 클릭 리스너를 체크박스에 직접 설정
        holder.completeCheckbox.setOnClickListener {
            //할 일 항목의 완료 상태를 토글
            todo.isDone =!todo.isDone
            //UI 업데이트를 위해 notifyItemChanged() 호출
            notifyItemChanged(position)
        }

        //항목을 길게 눌렀을때 이벤트 처리
        holder.itemView.setOnClickListener {
            onItemLongClick(todo, position)
            true // 이벤트 소비

        }

        //완료 상태에 따른 UI업데이트
        if (todo.isDone) {
            holder.todoTitle.paintFlags = holder.todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.todoTitle.setTextColor(0xFF888888.toInt()) // 회색
        } else {
            holder.todoTitle.paintFlags = holder.todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.todoTitle.setTextColor(0xFF000000.toInt()) // 검은색
        }
    }

    override fun getItemCount() = todoList.size
}