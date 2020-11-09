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
    private val values: List<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.findViewById(R.id.item_number)
        val contentView: TextView = itemView.findViewById(R.id.content)

        fun bind(position: Int) {
            idView.text = (position + 1).toString()
            contentView.text = values[position]
            itemView.setOnClickListener {
                onItemClickListener.onClick(contentView.text.toString())
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}