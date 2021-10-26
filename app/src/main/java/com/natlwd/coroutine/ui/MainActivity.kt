package com.natlwd.coroutine.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.natlwd.coroutine.databinding.ActivityMainBinding
import com.natlwd.coroutine.extensions.handleStateWithLoading
import com.natlwd.coroutine.utils.viewBinding

class MainActivity : BasActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupObserve()
        setupListener()

        model.getUsers("1")
    }

    private fun setupObserve() {
        model.userLiveData.observe(this, { state ->
            state.handleStateWithLoading(
                this,
                complete = {
                    binding.mainActivityTextView.text = it?.title
                },
                failed = {
                    binding.mainActivityTextView.text = it.message
                }
            )
        })
    }

    private fun setupListener() {
        binding.mainActivityButton.setOnClickListener {
            model.getUsers("2")
        }
    }
}