package com.delirium.reader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.delirium.reader.databinding.NewsReadingBinding
import com.delirium.reader.parser.GetNews
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL

class NewsReading : Fragment() {
    private val getNews = GetNews()
    private var text: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<NewsReadingBinding>(inflater,
            R.layout.news_reading, container, false)

        /*runBlocking {
            GlobalScope.launch {
                text = getNews.getDescription(URL(arguments?.getString("link")))!!
            }.join()
        }*/

        /*binding.news = text?.let {
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        }*/
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(arguments?.getString("link")))

        startActivity(intent)
        return binding.root
    }
}