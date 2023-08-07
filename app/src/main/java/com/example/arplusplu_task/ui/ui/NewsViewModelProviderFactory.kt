package com.example.arplusplu_task.ui.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arplusplu_task.ui.repository.NewsRepository

class NewsViewModelProviderFactory(
   val newsRepository: NewsRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}