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
import androidx.recyclerview.widget.GridLayoutManager
import com.delirium.reader.databinding.SourcesListBinding

class SourcesList : Fragment() {

    //TODO Must be stored in other place
    private var animalList : List<Source> = listOf(
        Source("Lenta", "https://lenta.ru/rss/news"),
        Source("Meduza", "https://meduza.io/rss2"),
        Source("Habr", "https://habr.com/ru/rss"),
        Source("Phoronix", "https://www.phoronix.com/rss.php"))

    private lateinit var adapter: SourceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<SourcesListBinding>(inflater,
            R.layout.sources_list, container, false)

        adapter = SourceAdapter(animalList, SourceListener { source ->
            Log.i("CLICK", "Click: ${source.name}")
            val bundle = bundleOf("name" to source.name)
            bundle.putString("link", source.link)

            binding.root.findNavController().navigate(
                R.id.action_sourcesList_to_newsList, bundle
            )
        })
        val manager = GridLayoutManager(activity, 2)
        binding.recycler.layoutManager = manager
        binding.recycler.setHasFixedSize(true)

        binding.recycler.adapter = adapter
        return binding.root
    }
}