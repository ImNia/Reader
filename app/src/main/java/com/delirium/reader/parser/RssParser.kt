package com.delirium.reader.parser

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class RssParser {
    private val rssItems = ArrayList<NewsFeed>()
    private var newsFeed: NewsFeed? = null
    private var text: String? = null

    fun parse(inputStream: InputStream) : List<NewsFeed> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var foundItem = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        foundItem = true
                        newsFeed = NewsFeed()
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                        newsFeed?.let { rssItems.add(it) }
                        foundItem = false
                        } else if ( foundItem && tagname.equals("title", ignoreCase = true)) {
                            newsFeed!!.title = text.toString()
                        } else if (foundItem && tagname.equals("link", ignoreCase = true)) {
                            newsFeed!!.link = text.toString()
                        } else if (foundItem && tagname.equals("description", ignoreCase = true)) {
                            newsFeed!!.description = text.toString()
                        }
                }
                eventType = parser.next()
            }
        } catch (ex: XmlPullParserException) {
            ex.printStackTrace()
        }

        return rssItems
    }
}