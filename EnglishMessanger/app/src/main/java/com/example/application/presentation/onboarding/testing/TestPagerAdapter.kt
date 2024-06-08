package com.example.application.presentation.onboarding.testing

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.application.data.model.Question

class TestPagerAdapter(fragment: Fragment, questions: List<Question>) :
    FragmentStateAdapter(fragment) {
    private val pages = questions

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        val question = pages[position]
        return TestPageFragment.newInstance(question, position, pages.size)
    }
}