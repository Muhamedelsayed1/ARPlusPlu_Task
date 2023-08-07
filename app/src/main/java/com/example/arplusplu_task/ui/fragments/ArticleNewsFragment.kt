package com.example.arplusplu_task.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.arplusplu_task.R
import com.example.arplusplu_task.databinding.FragmentArticleBinding
import com.example.arplusplu_task.databinding.FragmentBreakingNewsBinding
import com.example.arplusplu_task.ui.MainActivity
import com.example.arplusplu_task.ui.NewsViewModel
import com.example.arplusplu_task.ui.adapter.NewsAdapter
import com.google.android.material.snackbar.Snackbar

class ArticleNewsFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args : ArticleNewsFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        binding.webView.apply {
            //this to make sure that page will always load inside this webview
            article.url?.let { loadUrl(it) }
        }
        binding.fab.setOnClickListener(){
            viewModel.saveArticles(article)
            Snackbar.make(view,"Article Saved Successfully",Snackbar.LENGTH_SHORT).show()
        }
}
}