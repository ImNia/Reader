package com.delirium.reader.news

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.NewsItemBinding
import com.delirium.reader.model.NewsFeed

class NewsAdapter(private val clickListener: NewsListener)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var dataSet: List<NewsFeed> = listOf()

    class ViewHolder(var binding: NewsItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var clickNews: NewsListener
        fun bind(word: NewsFeed, clickListener: NewsListener) {
            binding.titleNews.text = word.title
            binding.titleNews.isClickable
            binding.titleNews.setOnClickListener(this)

            binding.source.text = word.source
                ?.substringAfter("//")
                ?.substringBefore("/")

            binding.releaseDate.text = word.releaseDate
            clickNews = clickListener
        }

        override fun onClick(p0: View?) {
//            clickNews.onClickNewsTitle((p0 as AppCompatTextView).text as String)
            clickNews.onClickNewsTitle(
                (p0 as AppCompatTextView).text as String,
                (binding.source as AppCompatTextView).text as String
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = dataSet[position]
        holder.bind(tmp, clickListener)
    }

    override fun getItemCount() : Int {
        return dataSet.size
    }
}

interface NewsListener {
    fun onClickNewsTitle(title: String, source: String)
}