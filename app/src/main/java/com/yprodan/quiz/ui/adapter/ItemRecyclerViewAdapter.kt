package com.yprodan.quiz.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.OnItemClickListener


/**
 * [RecyclerView.Adapter] that can display a [String].
 */
class ItemRecyclerViewAdapter(
    private val fileNamesList: List<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = fileNamesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.item_number)
        private val contentTextView: TextView = itemView.findViewById(R.id.content)

        fun bind(position: Int) {
            // position + 1 because the countdown starts with 0, but need 1
            idTextView.text = (position + 1).toString()
            contentTextView.text = fileNamesList[position]
            itemView.setOnClickListener {
                onItemClickListener.onClick(contentTextView.text.toString())
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentTextView.text + "'"
        }
    }
}