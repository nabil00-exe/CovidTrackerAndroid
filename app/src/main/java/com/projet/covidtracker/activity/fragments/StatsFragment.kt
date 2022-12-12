package com.projet.covidtracker.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.projet.covidtracker.R



class StatsFragment : Fragment() {

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_stats, container, false)

        val thiscontext = container!!.getContext();
        val heatmap =viewOfLayout.findViewById<WebView>(R.id.heatmap)

        heatmap.settings.javaScriptEnabled = true

        heatmap.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        heatmap.loadUrl("https://app.developer.here.com/coronavirus/")
        //heatmap.loadUrl("https://www.arcgis.com/apps/dashboards/85320e2ea5424dfaaa75ae62e5c06e61")

        return viewOfLayout
    }

}