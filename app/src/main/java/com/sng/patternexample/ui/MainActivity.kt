package com.sng.patternexample.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sng.patternexample.databinding.ActivityMainBinding
import com.sng.patternexample.model.Blog
import com.sng.patternexample.utils.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: BlogViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        subscribeObservers()
        viewModel.setStateEvent(BlogViewModel.MainStateEvent.GetBlogEvent)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success<List<Blog>> -> {
                    handleProgress(false)
                    appendBlogTitles(dataState.data)
                }
                is DataState.Error -> {
                    handleProgress(false)
                    showMessage(dataState.exception.localizedMessage)
                }
                is DataState.Loading -> {
                    handleProgress(true)
                }
            }
        })
    }

    private fun showMessage(message: String?) {
        if (message?.isNotEmpty() == true) {
            binding.text.text = message
        } else {
            binding.text.text = "Error without message"
        }
    }

    private fun handleProgress(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun appendBlogTitles(blogs: List<Blog>) {
        val sb = StringBuilder()
        for (blog in blogs) {
            sb.append(blog.title + "\n")
        }
        binding.text.text = sb.toString()
    }
}