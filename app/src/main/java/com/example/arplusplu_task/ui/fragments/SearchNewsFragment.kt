package com.example.arplusplu_task.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.arplusplu_task.R
import com.example.arplusplu_task.ui.MainActivity
import com.example.arplusplu_task.ui.NewsViewModel

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit  var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }
}