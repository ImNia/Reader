package com.delirium.reader

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.delirium.reader.databinding.NewsListBinding
import com.delirium.reader.parser.NewsFeed
import com.delirium.reader.parser.RssFeedFetcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL

class NewsList : Fragment() {

    private val feedFetcher = RssFeedFetcher()
    private lateinit var newsAdapter: NewsAdapter
    private var newsTitle: List<NewsFeed> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<NewsListBinding>(inflater,
            R.layout.news_list, container, false)

        Log.i("NEWS_LIST", "${arguments?.getString("link")}")

        runBlocking {
            GlobalScope.launch {
                newsTitle = feedFetcher.getTitleNews(URL(arguments?.getString("link")))!!
            }.join()
            Log.i("NEWS_LIST", "In runBlocking: ${newsTitle.size}")
        }

        newsAdapter = NewsAdapter(newsTitle, NewsListener { news ->
            val bundle = bundleOf("title" to news.title)
            bundle.putString("description", news.description)
            bundle.putString("link", news.link)

            binding.root.findNavController().navigate(
                R.id.action_newsList_to_newsReading, bundle
            )
        })

        val manager = LinearLayoutManager(activity)
        binding.recyclerNewsList.layoutManager = manager
        binding.recyclerNewsList.setHasFixedSize(true)

        binding.recyclerNewsList.adapter = newsAdapter

        Log.i("NEWS_LIST", "Not in runBlocking: ${newsTitle.size}")
        return binding.root
    }
}
