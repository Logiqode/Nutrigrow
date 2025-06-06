package com.example.nutrigrow.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nutrigrow.R
import com.example.nutrigrow.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Observe the response
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apiResponse.collect { response ->
                    if (response != null) {
                        println("Success! Data: ${response.message}")
                    } else {
                        println("Failed to fetch data")
                    }
                }
            }
        }

        // Trigger the fetch
        viewModel.fetchData("test")

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}