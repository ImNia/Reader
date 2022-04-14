package com.delirium.reader.news

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.NewsItemBinding
import com.delirium.reader.model.NewsFeed

class NewsAdapter(private val clickListener: NewsListener)
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var dataSet: List<NewsFeed> = listOf()

    class ViewHolder(var binding: NewsItemBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var clickNews: NewsListener
        fun bind(currentNews: NewsFeed, clickListener: NewsListener) {
            binding.titleNews.text = currentNews.title
            binding.newsItemCard.isClickable
            binding.newsItemCard.setOnClickListener(this)

            binding.source.text = currentNews.source
                ?.substringAfter("//")
                ?.substringBefore("/")

            binding.releaseDate.text = currentNews.releaseDate
            if (currentNews.isFavorite)
                binding.favoriteIndicator.setImageResource(R.drawable.ic_favorite_black_24dp)
            else binding.favoriteIndicator.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            binding.favoriteIndicator.isClickable
            binding.favoriteIndicator.setOnClickListener(this)
            clickNews = clickListener
        }

        override fun onClick(p0: View?) {
            if (binding.favoriteIndicator.id == p0?.id) {
                clickNews.onClickFavorite(
                    binding.titleNews.text as String,
                    binding.source.text as String
                )
            } else {
                clickNews.onClickNewsTitle(
                    binding.titleNews.text as String,
                    binding.source.text as String
                )
            }
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
    fun onClickFavorite(title: String, source: String)
}