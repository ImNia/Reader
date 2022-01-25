package com.delirium.reader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

class NewsReading : Fragment() {
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(R.layout.news_reading, container, false)

        webView = rootView.findViewById(R.id.newsWebView)
        webView.webViewClient = NewsWebViewClient()
        webView.settings.javaScriptEnabled

        arguments?.getString("link")?.let{
            webView.loadUrl(it)
        }
        return rootView
    }
}