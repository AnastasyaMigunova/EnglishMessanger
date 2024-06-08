package com.example.application.presentation.onboarding.interests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentChooseInterestsBinding
import com.example.application.`interface`.OnItemClickListener
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class ChooseInterestsFragment : Fragment(R.layout.fragment_choose_interests) {

    private val binding by viewBinding(FragmentChooseInterestsBinding::bind)
    private lateinit var viewModel: InterestsViewModel
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewInterests.layoutManager = layoutManager

        getInterests()
    }

    private fun getInterests() {
        lifecycleScope.launch {
            try {
                val interests = ApiClient.service.getInterests()

                viewModel = ViewModelProvider(requireActivity())[InterestsViewModel::class.java]
                itemsAdapter = ItemsAdapter(viewModel)

                binding.recyclerViewInterests.adapter = itemsAdapter.apply {
                    listener = object : OnItemClickListener {
                        override fun onItemClick(position: Int) {}
                        override fun onItemSetClick(cardSet: CardSet) {}
                        override fun onItemExerciseClick(rightAnswer: String, currentAnswer: String) {
                        }
                    }
                }
                itemsAdapter.setList(interests)
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): ChooseInterestsFragment {
            return ChooseInterestsFragment()
        }
    }
}