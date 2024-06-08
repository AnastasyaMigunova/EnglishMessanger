package com.example.application.presentation.user.main_exercises.theory.category

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Category
import com.example.application.data.model.ListItem
import com.example.application.data.model.Subtopic
import com.example.application.data.model.Theory
import com.example.application.data.model.Topic
import com.example.application.databinding.FragmentCategoryBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnTheoryItemClick
import com.example.application.presentation.user.main_exercises.theory.ItemsAdapter
import com.example.application.presentation.user.main_exercises.theory.TheoryInformationFragment

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)
    private lateinit var category: Category
    private val itemsAdapter = ItemsAdapter()
    private var buttonListener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        arguments.let {
            category = it?.getParcelable(CATEGORY)!!
        }

        binding.textViewCategoryTopic.text = category.title

        binding.recyclerViewItemsCategory.adapter = itemsAdapter.apply {
            listener = object : OnTheoryItemClick {
                override fun onCategoryClick(category: Category) {}
                override fun onTopicClick(topic: Topic) {
                    binding.textViewCategoryTopic.text = topic.title
                    val convertedItems = if (topic.subtopicList.isNotEmpty()) {
                        topic.subtopicList.map { subtopic ->
                            ListItem.SubtopicItem(subtopic)
                        }
                    } else {
                        topic.theoryList.map { theory ->
                            ListItem.TheoryItem(theory)
                        }
                    }

                    itemsAdapter.setList(convertedItems)
                }

                override fun onSubtopicClick(subtopic: Subtopic) {
                    val convertedItems = subtopic.theoryList.map { theory ->
                        ListItem.TheoryItem(theory)
                    }

                    itemsAdapter.setList(convertedItems)
                }

                override fun onTheoryListItemClick(theory: Theory) {
                    buttonListener?.onButtonCLick(
                        TheoryInformationFragment.newInstance(theory),
                        "THEORY_INFORMATION"
                    )
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItemsCategory.layoutManager = layoutManager

        val convertedItems = if (category.topics.isNotEmpty()) {
            category.topics.map { topic ->
                ListItem.TopicItem(topic)
            }
        } else {
            category.theoryList.map { theory ->
                ListItem.TheoryItem(theory)
            }
        }

        itemsAdapter.setList(convertedItems)
    }

    companion object {
        private const val CATEGORY = "category"

        fun newInstance(category: Category): CategoryFragment =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }
}