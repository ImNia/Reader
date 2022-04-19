package com.delirium.reader.favorite

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.FragmentFavoriteBinding
import com.delirium.reader.model.NewsFeed
import com.google.android.material.appbar.MaterialToolbar

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
        setHasOptionsMenu(true)
        val toolBar: MaterialToolbar? = activity?.findViewById(R.id.toolBar)

        val menuFavorite = toolBar?.menu?.findItem(R.id.favorite)
        menuFavorite?.isVisible = false

        toolBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    Log.i("MENU", "Click on filter in Favorite")
                    true
                }
                else -> {
                    false
                }
            }
        }

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

    fun selectNewsForReading(news: NewsFeed) {
        bindingNews.root.findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToNewsReading(
                news.link ?: "ERROR",
                news.source
                    ?.substringAfter("//")
                    ?.substringBefore("/") ?: "???"
            )
        )
    }

    override fun onClickNews(title: String) {
        favoritePresenter.selectNewsFromList(title)
    }

    override fun onClickFavorite(title: String) {
        favoritePresenter.deleteNews(title)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("FAVORITE", "In function onOptionItemSelected")
        NavigationUI.onNavDestinationSelected(item, findNavController())
        return when (item.itemId) {
            R.id.favorite -> {
                Log.i("FAVORITE", "Click favorite")
                true
            }
            else -> false
        }
    }
}