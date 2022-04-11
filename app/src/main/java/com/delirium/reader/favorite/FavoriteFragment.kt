package com.delirium.reader.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.FragmentFavoriteBinding
import com.delirium.reader.databinding.NewsListBinding
import com.delirium.reader.model.NewsFeed
import com.delirium.reader.news.NewsAdapter
import com.delirium.reader.news.NewsListPresenter

class FavoriteFragment : Fragment(), ClickFavoriteNews {
    private lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var bindingNews: FragmentFavoriteBinding

    private var recyclerView: RecyclerView? = null
    private lateinit var linearManager: LinearLayoutManager
    private val favoritePresenter: FavoritePresenter by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingNews = FragmentFavoriteBinding.inflate(inflater, container, false)

        linearManager = LinearLayoutManager(activity)
        recyclerView = bindingNews.recyclerFavorite
        recyclerView?.layoutManager = linearManager

        return bindingNews.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter(this)
        recyclerView?.adapter = favoriteAdapter
        favoritePresenter.attachViewAndInit(this)
        favoritePresenter.currentState()
    }

    override fun onResume() {
        super.onResume()
        bindingNews.goToFilms.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
    }

    fun drawNewsList(news: List<NewsFeed>) {
        if (news.isNotEmpty()) {
            bindingNews.textWithoutFilms.visibility = View.INVISIBLE
            bindingNews.goToFilms.visibility = View.INVISIBLE
        }
        favoriteAdapter.dataSet = news
        favoriteAdapter.notifyDataSetChanged()
    }

    fun showNotNews() {
        bindingNews.textWithoutFilms.visibility = View.VISIBLE
        bindingNews.goToFilms.visibility = View.VISIBLE
    }

    override fun onClickNews(name: String) {
        Log.i("FAVORITE_ADAPTER", "click on $name")
    }

    override fun onClickFavorite(name: String) {
        favoritePresenter.deleteNews(name)
    }
}