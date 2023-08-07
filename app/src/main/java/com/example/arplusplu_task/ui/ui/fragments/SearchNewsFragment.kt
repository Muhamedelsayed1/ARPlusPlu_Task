package com.example.arplusplu_task.ui.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arplusplu_task.R

import com.example.arplusplu_task.databinding.FragmentSearchNewsBinding
import com.example.arplusplu_task.ui.ui.MainActivity
import com.example.arplusplu_task.ui.ui.NewsViewModel
import com.example.arplusplu_task.ui.adapter.NewsAdapter
import com.example.arplusplu_task.ui.util.Constants
import com.example.arplusplu_task.ui.util.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var binding: FragmentSearchNewsBinding
    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        newsAdapter.setOnItemClickListener {
            var bundle = Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleNewsFragment,
                bundle
            )
        }

        //now we will create a little delay until we make the actual request
        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
               delay(500L)
            }
            editable?.let {
                if (editable.toString().isNotEmpty()){
                    viewModel.searchNews(editable.toString())
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resources.Success->{
                    hideProgressBar()
                    //now to check if the data != null
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        var totalPages = newsResponse.totalResults/ Constants.QUERY_PAGE_SIZE +2
                        isLastPage = viewModel.searchNewsPage==totalPages
                        if (isLastPage){
                                binding.rvSearchNews.setPadding(0,0,0,0 )

                        }


                    }
                }
                is Resources.Error->{
                    hideProgressBar()
                    response.message?.let{message->
                        Log.e(TAG,"An Error occured: $message")

                    }
                }
                is Resources.Loading->{
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        var isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        var isLoading = true
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListner = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // this condition means that we currently scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //using this variables we can check if we scroll to the bottom in RecyclerView
            var layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            var totalItemCount = layoutManager.itemCount
            val isNotLoadingAndLastPage = !isLastPage &&  !isLoading
            val isAtLastItem = firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount>= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndLastPage&&isLastPage && isNotAtBeginning && isTotalMoreThanVisible
                    && isScrolling
            if(shouldPaginate){
                viewModel.searchNews(binding.etSearch.toString())
                isScrolling = false
            }
        }

    }
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListner)
        }
    }
}