package com.example.arplusplu_task.ui.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arplusplu_task.ui.models.Article
import com.example.arplusplu_task.ui.models.NewsResponse
import com.example.arplusplu_task.ui.repository.NewsRepository
import com.example.arplusplu_task.ui.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) :ViewModel(){
    val breakingNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var breakingNewsPage =1
    val searchNews: MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var searchNewsPage =1
    var breakingNewsResponse:NewsResponse?=null

    var searchNewsResponse:NewsResponse?=null

    init {
        getBreakingNews("us")
    }
    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        //response refer to response we get from retrofit
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handelBreakingNewsResponse(response))
    }
    fun searchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resources.Loading())
        val response = newsRepository.getBreakingNews(searchQuery,breakingNewsPage)
        searchNews.postValue(handelBreakingNewsResponse(response))
    }

    private fun handelBreakingNewsResponse(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                breakingNewsPage++
                if (breakingNewsResponse==null){
                    breakingNewsResponse= resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resources.Success(breakingNewsResponse?:resultResponse)


            }

        }
        return Resources.Error(response.message())
    }
    private fun handelSearchNewsResponse(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                searchNewsPage ++
                if (searchNewsResponse==null){
                    searchNewsResponse= resultResponse
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resources.Success(searchNewsResponse?:resultResponse)


            }

        }
        return Resources.Error(response.message())
    }
    fun saveArticles(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}