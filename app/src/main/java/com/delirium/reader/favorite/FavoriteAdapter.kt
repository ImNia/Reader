package com.delirium.reader.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.NewsItemBinding
import com.delirium.reader.model.NewsFeed

class FavoriteAdapter(
    private val clickListener: ClickFavoriteNews
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var dataSet: List<NewsFeed> = listOf()

    class ViewHolder(var binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        private lateinit var clickNews: ClickFavoriteNews
        fun bind(currentNews: NewsFeed, clickListener: ClickFavoriteNews) {
            binding.titleNews.text = currentNews.title
            binding.titleNews.isClickable
            binding.titleNews.setOnClickListener(this)

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
                    binding.titleNews.text as String
                )
            } else {
                clickNews.onClickNews(
                    binding.titleNews.text as String
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = dataSet[position]
        holder.bind(tmp, clickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}

interface ClickFavoriteNews {
    fun onClickNews(title: String)
    fun onClickFavorite(title: String)
}