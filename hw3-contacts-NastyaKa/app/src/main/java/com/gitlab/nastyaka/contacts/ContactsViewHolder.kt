package com.gitlab.nastyaka.contacts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactsViewHolder(person: View) : RecyclerView.ViewHolder(person) {
    private val personNameView : TextView = person.contact_name
    val personNumberView : TextView = person.contact_number

    fun bind(contact: Contact) {
        personNameView.text = contact.name
        personNumberView.text = contact.phoneNumber
    }
}