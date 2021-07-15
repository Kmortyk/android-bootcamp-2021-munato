package com.example.munato.fragment.logic

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient

fun getHTMLPageTemplate(context: Context) : String {
    val stream = context.resources.assets.open("templates/page_template.html")
    return stream.readBytes().decodeToString()
}

fun configureWebView(ctx: Context, w: WebView) {
    w.webViewClient = WebViewClient()
}
