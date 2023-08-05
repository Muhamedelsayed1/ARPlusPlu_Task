package com.example.arplusplu_task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.arplusplu_task.R
import com.example.arplusplu_task.databinding.ActivityMainBinding
import com.example.arplusplu_task.ui.db.ArticleDataBase
import com.example.arplusplu_task.ui.repository.NewsRepository

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository =NewsRepository(ArticleDataBase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel= ViewModelProvider(this,newsViewModelProviderFactory).get(NewsViewModel::class.java)

        //binding the navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}