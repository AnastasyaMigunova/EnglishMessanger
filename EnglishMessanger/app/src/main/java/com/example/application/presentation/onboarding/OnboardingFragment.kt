package com.example.application.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentOnboardingBinding
import com.example.application.`interface`.OnSkipItemListener
import com.example.application.presentation.onboarding.interests.ChooseInterestsFragment
import com.example.application.presentation.onboarding.testing.CheckLevelFragment

class OnboardingFragment : Fragment(R.layout.fragment_onboarding), OnSkipItemListener {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(UsernameFragment.newInstance())
        adapter.addFragment(DateBirthFragment.newInstance())

        val photoFragment = PhotoFragment.newInstance()
        photoFragment.setOnSkipItemListener(this)
        adapter.addFragment(photoFragment)

        adapter.addFragment(ChooseInterestsFragment.newInstance())
        adapter.addFragment(CheckLevelFragment.newInstance())

        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager(binding.viewPager)
    }

    override fun onSkipItemClick() {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }


    companion object {
        fun newInstance() : OnboardingFragment {
            return OnboardingFragment()
        }
    }
}