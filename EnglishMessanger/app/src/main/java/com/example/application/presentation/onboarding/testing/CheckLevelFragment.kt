package com.example.application.presentation.onboarding.testing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentCheckLevelBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.onboarding.interests.InterestsViewModel

class CheckLevelFragment : Fragment(R.layout.fragment_check_level) {

    private val binding by viewBinding(FragmentCheckLevelBinding::bind)
    private val fragment = BannerFragment.newInstance()
    private var listener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCheckLevel.setOnClickListener {
            listener?.onButtonCLick(fragment, "BANNER")
        }
    }

    companion object {
        fun newInstance(): CheckLevelFragment {
            return CheckLevelFragment()
        }
    }
}