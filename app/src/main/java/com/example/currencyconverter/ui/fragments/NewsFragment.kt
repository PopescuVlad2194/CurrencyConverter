package com.example.currencyconverter.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.adapters.NewsAdapter
import com.example.currencyconverter.ui.NewsViewModel
import com.example.currencyconverter.util.Resource
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment(R.layout.fragment_news) {

        lateinit var viewModel: NewsViewModel
        lateinit var newsAdapter: NewsAdapter


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = (activity as MainActivity).newsViewModel
            setupRecyclerView()

            newsAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", it)
                }
                findNavController().navigate(
                    R.id.action_newsFragment_to_articleFragment,
                    bundle
                )
            }

            viewModel.latestNews.observe(viewLifecycleOwner, { response ->
                when(response) {
                    is Resource.Success -> {
                        response.data?.let { newsResponse ->
                            newsAdapter.differ.submitList(newsResponse.articles)
                        }
                    }
                }
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