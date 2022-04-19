package com.delirium.reader.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.fragment.navArgs
import com.delirium.reader.R
import com.google.android.material.appbar.MaterialToolbar

class NewsReading : Fragment() {
    private val args by navArgs<NewsReadingArgs>()
    private val linkNews by lazy { args.newsLink }
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val toolbar: MaterialToolbar? = activity?.findViewById(R.id.toolBar)
        val menuFilter = toolbar?.menu?.findItem(R.id.filter)
        menuFilter?.isVisible = false
        val menuFavorite = toolbar?.menu?.findItem(R.id.favorite)
        menuFavorite?.isVisible = false

        val bindingView = inflater.inflate(R.layout.news_reading, container, false)

        webView = bindingView.findViewById(R.id.newsWebView)
        webView.webViewClient = NewsWebViewClient()
        webView.settings.javaScriptEnabled

        webView.loadUrl(linkNews)

        return bindingView
    }
}