package com.gitlab.nastyaka.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatsListAdapter(
    private val context: Context,
    private val navigate: (String) -> Unit
) : ListAdapter<Chat, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.name)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newHolder = holder as ViewHolder
        newHolder.name.text = getItem(position).name
        newHolder.itemView.setOnClickListener { navigate(getItem(position).name) }
    }
}

private class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {

    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.name == newItem.name
    }
}