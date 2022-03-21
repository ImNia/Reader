package com.delirium.reader.sources

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.SourceItemBinding

class SourceAdapter(private val clickListener: SourceListener)
    : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    var dataSet: List<Source> = listOf()

    class ViewHolder(binding: SourceItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        private lateinit var clickSource: SourceListener
        private val bindingNews: SourceItemBinding = binding

        fun bind(sourceNews: Source, clickListener: SourceListener) {
            Log.i("ADAPTER", sourceNews.name)
            bindingNews.nameSource.text = sourceNews.name
            bindingNews.nameSource.isClickable
            bindingNews.nameSource.setOnClickListener(this)

            clickSource = clickListener
        }

        override fun onClick(p0: View?) {
            clickSource.onClickSource((p0 as AppCompatTextView).text as String)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SourceItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sourceNews = dataSet[position]
        Log.i("ADAPTER", "$sourceNews")
        holder.bind(sourceNews, clickListener)
    }

    override fun getItemCount() : Int {
        return dataSet.size
    }
}

interface SourceListener {
    fun onClickSource(nameSource: String)
}