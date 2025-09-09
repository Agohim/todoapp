package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//class TodoAdapter(private val todoList: List<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
class TodoAdapter(private val todoList: MutableList<Todo>, private val onItemClick: (Todo) -> Unit) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoTitle: TextView = view.findViewById(R.id.todo_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_list_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoTitle.text = todo.title

        //항목 클릭 이벤트 리스너
        holder.itemView.setOnClickListener {
            onItemClick(todo)
            //클릭 시 바로 UI를 업데이트하기 위해 notifyItemChanged() 호출
            notifyItemChanged(position)
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