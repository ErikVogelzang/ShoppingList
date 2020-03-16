package com.example.shoppinglist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.model.ListItem
import kotlinx.android.synthetic.main.list_item.view.*

class ListItemAdapter(val listItems: List<ListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.list_item,
            parent,
            false
        )
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListItemViewHolder).bind(listItems[position])
    }

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(listItem: ListItem) {
            itemView.tvItem.text = "${listItem.count} ${listItem.name}"
        }
    }
}