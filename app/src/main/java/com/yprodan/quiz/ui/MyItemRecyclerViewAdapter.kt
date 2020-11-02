package com.yprodan.quiz.ui

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yprodan.quiz.R
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.InformationReceiver


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 */
class MyItemRecyclerViewAdapter(
    private val values: List<String>,
    private val parentActivity: Activity
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idView.text = (position + 1).toString()
        holder.contentView.text = values[position]
        Log.d("fname", holder.contentView.text.toString())
        holder.itemView.setOnClickListener {
            (parentActivity as InformationReceiver).transmitFileNameToActivity(
                holder.contentView.text.toString()
                        + Constants.ABBREVIATION_TAG
            )
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}