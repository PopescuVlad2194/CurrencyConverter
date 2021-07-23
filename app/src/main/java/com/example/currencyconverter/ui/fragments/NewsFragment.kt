package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.adapters.NewsAdapter
import com.example.currencyconverter.models.news.Article
import com.example.currencyconverter.ui.NewsViewModel
import com.example.currencyconverter.util.Constants.TAG
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var list: List<Article> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).newsViewModel
        setupRecyclerView()



        newsAdapter.setOnItemClickListener { articleClicked ->
            articleClicked.seen = true
            viewModel.updateArticle(articleClicked.url, articleClicked.seen)
            val bundle = Bundle().apply {
                putSerializable("article", articleClicked)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.storageNews.observe(viewLifecycleOwner, { articleList ->
            newsAdapter.differ.submitList(articleList)
        })

        viewModel.getNewsLocally().observe(viewLifecycleOwner, {
            viewModel.storageNews.value = it
            list = it
            viewModel.latestNews.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { newsResponse ->
                            val remoteNews = newsResponse.articles
                            if (list != null) {
                                for (article in remoteNews) {
                                    var found = false
                                    for (localArticle in list) {
                                        if (article.url == localArticle.url) {
                                            Log.d(TAG, article.toString())
                                            found = true
                                        }
                                    }
                                    if (!found) {
                                        viewModel.saveArticle(article)
                                    }
                                }
                            } else {
                                for (article in remoteNews) {
                                    viewModel.saveArticle(article)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.e(TAG, "Error newsFragment: $message")
                        }
                    }
                }
            })

        })





    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}