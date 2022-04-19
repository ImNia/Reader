package com.delirium.reader.news

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.NewsListBinding
import com.delirium.reader.model.NewsFeed
import com.delirium.reader.sources.Source
import com.google.android.material.appbar.MaterialToolbar

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

        setHasOptionsMenu(true)
        val toolBar: MaterialToolbar? = activity?.findViewById(R.id.toolBar)

        val menuFavorite = toolBar?.menu?.findItem(R.id.favorite)
        menuFavorite?.isVisible = true

        toolBar?.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.favorite -> {
                    bindingNews.root.findNavController().navigate(
                        NewsListDirections.actionNewsListToFavoriteFragment()
                    )
                    true
                }
                R.id.filter -> {
                    clickOnFilter(activity?.findViewById(R.id.filter))
                    true
                }
                else -> false
            }
        }

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

    fun drawNewsList(news: List<NewsFeed>) {
        newsAdapter.dataSet = news
        newsAdapter.notifyDataSetChanged()
    }

    fun selectedNewsTitle(title: NewsFeed) {
        bindingNews.root.findNavController().navigate(
            NewsListDirections.actionNewsListToNewsReading(
                title.link ?: "ERROR",
                title.source
                    ?.substringAfter("//")
                    ?.substringBefore("/") ?: "???"
            )
        )
    }

    override fun onClickNewsTitle(title: String, source: String) {
        newsListPresenter.selectNewsTitle(title, source)
    }

    override fun onClickFavorite(title: String, source: String) {
        newsListPresenter.selectFavoriteNews(title, source)
    }

    private fun clickOnFilter(viewForMenu: View?) {
        val popMenu = PopupMenu(activity, viewForMenu)
        popMenu.menu.add(getString(R.string.all_news_menu))
        for (newsSource in sourceList) {
            popMenu.menu.add(newsSource.name)
        }

        val menuInflater = popMenu.menuInflater
        menuInflater.inflate(R.menu.popup_menu_source, popMenu.menu)
        popMenu.show()

        popMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == getString(R.string.all_news_menu)) {
                filterNews(menuItem.title.toString())
            }
            sourceList.forEach { source ->
                if (menuItem.title == source.name) {
                    filterNews(source.name)
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
    }

    private fun filterNews(source: String) {
        Log.i("NEWS_LIST", "in filterNews function: $source")
        newsListPresenter.filterNews(source)
    }
}
