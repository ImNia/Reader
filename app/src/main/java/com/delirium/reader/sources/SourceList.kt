package com.delirium.reader.sources

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.delirium.reader.databinding.SourcesListBinding

class SourceList : Fragment(), SourceListener {

    private lateinit var adapter: SourceAdapter
    private var _bindingSource: SourcesListBinding? = null
    private val bindingSource get() = _bindingSource!!

    private var recyclerView: RecyclerView? = null

    private lateinit var gridManager: GridLayoutManager
    private val sourcePresenter: SourcePresenter by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingSource = SourcesListBinding.inflate(inflater, container, false)

        gridManager = GridLayoutManager(activity, 2)
        recyclerView = bindingSource.recycler
        recyclerView?.layoutManager = gridManager

        return bindingSource.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sourcePresenter.attachView(this)

        adapter = SourceAdapter(this)
        recyclerView?.adapter = adapter
        sourcePresenter.currentState()
    }

    override fun onClickSource(nameSource: String) {
        sourcePresenter.selectSource(nameSource)
    }

    fun drawSourceList(sourceList : List<Source>) {
        adapter.dataSet = sourceList
    }

    fun selectedSource(nameSource: Source) {
        bindingSource.root.findNavController().navigate(
            SourceListDirections.actionSourceListToNewsList(nameSource)
        )
    }
}