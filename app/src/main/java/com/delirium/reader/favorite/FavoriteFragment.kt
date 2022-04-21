package com.delirium.reader.favorite

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.R
import com.delirium.reader.databinding.FragmentFavoriteBinding
import com.delirium.reader.model.StatusCode
import com.delirium.reader.model.NewsFeed
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment(), ClickFavoriteNews {
    private lateinit var favoriteAdapter: FavoriteAdapter
    lateinit var bindingNews: FragmentFavoriteBinding

    private var recyclerView: RecyclerView? = null
    private lateinit var linearManager: LinearLayoutManager
    private val favoritePresenter: FavoritePresenter by activityViewModels()

    private val args by navArgs<FavoriteFragmentArgs>()
    private val sourceList by lazy { args.sourceList.toList() }

    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val toolBar: MaterialToolbar? = activity?.findViewById(R.id.toolBar)

        val menuFavorite = toolBar?.menu?.findItem(R.id.favorite)
        menuFavorite?.isVisible = false

        val menuFilter = toolBar?.menu?.findItem(R.id.filter)
        menuFilter?.isVisible = true

        toolBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> {
                    clickOnFilter(activity?.findViewById(R.id.filter))
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
        news.link?.let { link ->
            bindingNews.root.findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToNewsReading(
                    link,
                    news.source
                        ?.substringAfter("//")
                        ?.substringBefore("/")
                        ?: getString(R.string.title_description)
                )
            )
        } ?: showSnackBar(StatusCode.LINK_NOT_FOUND)
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
        favoritePresenter.filterNews(source)
    }

    fun showSnackBar(statusCode: StatusCode? = null) {
        val textError = when(statusCode) {
            StatusCode.NEWS_NOT_FOUND,
            StatusCode.NOT_FOUND -> getString(R.string.not_found_error)
            StatusCode.LINK_NOT_FOUND -> getString(R.string.not_found_link_error)
            StatusCode.CONFLICT_VALUE -> getString(R.string.conflict_value_error)
            StatusCode.NOT_CONNECT -> getString(R.string.data_not_load)
            StatusCode.REQUEST_TIMEOUT -> getString(R.string.request_timeout_error)
            else -> getString(R.string.unknown_error)
        }

        snackBar = Snackbar
            .make(bindingNews.recyclerFavorite, textError, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ok)) {
                snackBar?.dismiss()
            }
        snackBar?.show()
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