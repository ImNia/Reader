package com.delirium.reader

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.delirium.reader.databinding.NewsListBinding
import com.delirium.reader.parser.RssFeedFetcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class NewsList : Fragment() {

    private val feedFetcher = RssFeedFetcher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<NewsListBinding>(inflater,
            R.layout.news_list, container, false)

        binding.news = arguments?.getString("name")
        Log.i("NEWS_LIST", "${arguments?.getString("link")}")

        val newsTitle = GlobalScope.launch {
            val tmp = feedFetcher.getTitleNews(URL(arguments?.getString("link")))
            Log.i("NEWS_LIST", "title: ${tmp?.get(0)?.title}")
        }
        return binding.root
    }
}