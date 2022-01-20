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
import com.delirium.reader.databinding.SourcesListBindingImpl

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SourcesList.newInstance] factory method to
 * create an instance of this fragment.
 */
class SourcesList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var animalList : List<Source> = listOf(
        Source("Horse"),
        Source("Cat"),
        Source("Dog"),
        Source("Cow"))
    private lateinit var adapter: SourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<SourcesListBinding>(inflater,
            R.layout.sources_list, container, false)

        adapter = SourceAdapter(animalList, SourceListener { name ->
            Log.i("CLICK", "Click: $name")
            val bundle = bundleOf("name" to name)
            binding.root.findNavController().navigate(
//                SourcesListDirections.actionSourcesListToNewsList(name)
                R.id.action_sourcesList_to_newsList, bundle
            )
        })
        val manager = GridLayoutManager(activity, 3)
        binding.recycler.layoutManager = manager
        binding.recycler.setHasFixedSize(true)

        binding.recycler.adapter = adapter
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SourcesList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SourcesList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}