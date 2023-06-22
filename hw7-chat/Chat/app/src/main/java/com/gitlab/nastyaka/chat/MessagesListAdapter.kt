package com.gitlab.nastyaka.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class MessagesListAdapter(private val context: Context) :
    ListAdapter<Messages, RecyclerView.ViewHolder>(MessagesDiffCallback()) {

    private val formater = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())

    class ViewHolderTXT(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.msg_name)!!
        val messageText = view.findViewById<TextView>(R.id.msg_txt)!!
        val date = view.findViewById<TextView>(R.id.msg_date)!!
    }

    class ViewHolderIMG(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.msg_name)!!
        val messageImage = view.findViewById<ImageView>(R.id.msg_img)!!
        val date = view.findViewById<TextView>(R.id.msg_date)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Messages.FROM_ME_TXT -> {
                ViewHolderTXT(
                    LayoutInflater.from(context).inflate(R.layout.outgoing_message, parent, false)
                )
            }
            Messages.FROM_ME_PIC -> {
                ViewHolderIMG(
                    LayoutInflater.from(context).inflate(R.layout.outgoing_message, parent, false)
                )
            }
            Messages.FROM_OTHER_TXT -> {
                ViewHolderTXT(
                    LayoutInflater.from(context).inflate(R.layout.incoming_message, parent, false)
                )
            }
            else -> {
                ViewHolderIMG(
                    LayoutInflater.from(context).inflate(R.layout.incoming_message, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Messages.FROM_ME_TXT, Messages.FROM_OTHER_TXT -> {
                val newHolder = holder as ViewHolderTXT
                newHolder.name.text = getItem(position).from
                newHolder.messageText.visibility = View.VISIBLE
                newHolder.messageText.text = getItem(position).data?.text?.text!!
                newHolder.date.text = formater.format(Date(getItem(position).time!!))
            }
            else -> {
                val newHolder = holder as ViewHolderIMG
                newHolder.name.text = getItem(position).from
                newHolder.messageImage.visibility = View.VISIBLE
                newHolder.date.text = formater.format(Date(getItem(position).time!!))
                Glide.with(context)
                    .load("http://213.189.221.170:8008/thumb/${getItem(position).data?.image?.imageLink}")
                    .override(500)
                    .into(newHolder.messageImage)
                newHolder.messageImage.setOnClickListener {
                    Intent(context, OpenedImage::class.java).also {
                        it.putExtra("link", getItem(position).data?.image?.imageLink)
                        context.startActivity(it)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).from == Messages.MY_NAME) {
            if (getItem(position).data?.image?.imageLink == null) {
                Messages.FROM_ME_TXT
            } else {
                Messages.FROM_ME_PIC
            }
        } else {
            if (getItem(position).data?.image?.imageLink == null) {
                Messages.FROM_OTHER_TXT
            } else {
                Messages.FROM_OTHER_PIC
            }
        }
    }
}

private class MessagesDiffCallback : DiffUtil.ItemCallback<Messages>() {

    override fun areItemsTheSame(oldItem: Messages, newItem: Messages): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Messages, newItem: Messages): Boolean {
        return oldItem.id == newItem.id
    }
}