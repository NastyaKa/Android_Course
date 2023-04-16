package com.gitlab.nastyaka.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val contacts: List<Contact>, val onClick: (Contact) -> Unit) :
    RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contact,
                parent,
                false
            )
        ).apply {
            this.personNumberView.setOnClickListener {
                @Suppress("DEPRECATION")
                onClick(contacts[adapterPosition])
            }
        }
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) =
        holder.bind(contacts[position])
}