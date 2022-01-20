package com.delirium.reader

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.SourceItemBinding

class SourceAdapter(private val dataSet: List<Source>, private val clickListener: SourceListener)
    : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    class ViewHolder(var binding: SourceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Source, clickListener: SourceListener) {
            binding.source = word
            binding.executePendingBindings()
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SourceItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = dataSet[position]
        Log.i("ADAPTER", "Values: $tmp")
        holder.bind(tmp, clickListener)
    }

    override fun getItemCount() : Int {
        Log.i("ADAPTER", "$dataSet.size")
        return dataSet.size
    }
}

class SourceListener(val clickListener: (name: String) -> Unit) {
    fun onClick(item: String = "Something") {
        clickListener(item)
    }
}