package com.example.application.presentation.user.main_exercises.theory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.application.R
import com.example.application.data.model.Category
import com.example.application.data.model.GrammarTheory
import com.example.application.data.model.ListItem
import com.example.application.data.model.Subtopic
import com.example.application.data.model.Theory
import com.example.application.data.model.Topic
import com.example.application.databinding.FragmentTheoryBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnTheoryItemClick
import com.example.application.presentation.user.main_exercises.theory.category.CategoryFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class TheoryFragment : Fragment(R.layout.fragment_theory) {

    private val binding by viewBinding(FragmentTheoryBinding::bind)
    private val itemsAdapter = ItemsAdapter()
    private lateinit var theory: GrammarTheory
    private var buttonListener: OnButtonClickListener? = null
    private var categories = mutableListOf<ListItem.CategoryItem>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        getTheory()
    }

    private fun getTheory() {
        lifecycleScope.launch {

            Glide.with(requireContext())
                .asGif()
                .load(R.raw.racoon)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageViewGif)

            binding.imageViewGif.visibility = View.VISIBLE

            try {
                theory = ApiClient.service.getTheory()

                val categoryItems: List<ListItem.CategoryItem> = theory.categories.map { category ->
                    ListItem.CategoryItem(category)
                }
                categories.addAll(categoryItems)

                binding.recyclerViewItemsTheory.adapter = itemsAdapter.apply {
                    listener = object : OnTheoryItemClick {
                        override fun onCategoryClick(category: Category) {
                            buttonListener?.onButtonCLick(
                                CategoryFragment.newInstance(category),
                                "CATEGORY"
                            )
                        }

                        override fun onTopicClick(topic: Topic) {}
                        override fun onSubtopicClick(subtopic: Subtopic) {}
                        override fun onTheoryListItemClick(theory: Theory) {}
                    }
                }

                val layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewItemsTheory.layoutManager = layoutManager
                itemsAdapter.setList(categories)
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }

            binding.imageViewGif.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): TheoryFragment {
            return TheoryFragment()
        }
    }
}