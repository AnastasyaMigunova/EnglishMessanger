package com.example.application.presentation.onboarding.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentBannerBinding
import com.example.application.`interface`.OnNextPageListener
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class BannerFragment : Fragment(R.layout.fragment_banner), OnNextPageListener {

    private val binding by viewBinding(FragmentBannerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getQuestions()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val progressBar = binding.progressBar
                progressBar.progress = position + 1
            }
        })
    }

    override fun onNextPage(position: Int) {
        binding.viewPager.setCurrentItem(position, true)
    }

    private fun getQuestions() {
        lifecycleScope.launch {
            try {
                val questions = ApiClient.service.getQuestions()
                val pageAdapter = TestPagerAdapter(this@BannerFragment, questions)
                binding.viewPager.adapter = pageAdapter
                val progressBar = binding.progressBar
                progressBar.max = binding.viewPager.adapter?.itemCount ?: 0
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): BannerFragment {
            return BannerFragment()
        }
    }
}