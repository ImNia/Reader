package com.delirium.reader

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter(private val dataSet: List<String>)
    : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.nameSource)

        fun bind(word: String) {
            textView.text = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ADAPTER", "Create in adapter")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.source_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = dataSet[position]
        Log.i("ADAPTER", "Values: $tmp")
        holder.bind(tmp)
    }

    override fun getItemCount() : Int {
        Log.i("ADAPTER", "$dataSet.size")
        return dataSet.size
    }
}