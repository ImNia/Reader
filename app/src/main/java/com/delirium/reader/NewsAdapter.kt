package com.delirium.reader

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.NewsItemBinding
import com.delirium.reader.parser.NewsFeed

class NewsAdapter(private val dataSet: List<NewsFeed>, private val clickListener: NewsListener)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(var binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(word: NewsFeed, clickListener: NewsListener) {
            binding.news = word
            binding.executePendingBindings()
            binding.newsListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NewsItemBinding.inflate(layoutInflater, parent, false)
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

class NewsListener(val clickListener: (name: NewsFeed) -> Unit) {
    fun onClick(item: NewsFeed) {
        clickListener(item)
    }
}