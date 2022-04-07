package com.delirium.reader.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.NewsListBinding
import com.delirium.reader.model.NewsFeed
import com.delirium.reader.sources.Source

class NewsList : Fragment(), NewsListener {
    private lateinit var newsAdapter: NewsAdapter
    lateinit var bindingNews: NewsListBinding

    private var sourceList : List<Source> = listOf(
        Source("Lenta", "https://lenta.ru/rss/news/"),
//        Source("Meduza", "https://meduza.io/rss/all/"),
        Source("Habr", "https://habr.com/ru/rss/"),
        Source("Phoronix", "https://www.phoronix.com/rss.php/")
    )

    private var recyclerView: RecyclerView? = null
    private lateinit var linearManager: LinearLayoutManager
    private val newsListPresenter: NewsListPresenter by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNews = NewsListBinding.inflate(inflater, container, false)

        linearManager = LinearLayoutManager(activity)
        recyclerView = bindingNews.recyclerNewsList
        recyclerView?.layoutManager = linearManager

        return bindingNews.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsListPresenter.attachViewAndInit(this, sourceList)

        newsAdapter = NewsAdapter(this)
        recyclerView?.adapter = newsAdapter
        newsListPresenter.currentState()
    }

    fun drawNewsList(title: List<NewsFeed>) {
        newsAdapter.dataSet = title
        newsAdapter.notifyDataSetChanged()
    }

    fun selectedNewsTitle(title: NewsFeed) {
        bindingNews.root.findNavController().navigate(
            NewsListDirections.actionNewsListToNewsReading(title.link ?: "ERROR")
        )
    }

    override fun onClickNewsTitle(title: String, source: String) {
        newsListPresenter.selectNewsTitle(title, source)
    }
}
