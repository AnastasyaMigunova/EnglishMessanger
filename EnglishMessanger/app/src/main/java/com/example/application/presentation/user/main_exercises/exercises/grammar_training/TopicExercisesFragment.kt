package com.example.application.presentation.user.main_exercises.exercises.grammar_training

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.SentenceExercise
import com.example.application.databinding.FragmentTopicExercisesBinding
import com.example.application.`interface`.OnItemClickListener

class TopicExercisesFragment : Fragment(R.layout.fragment_topic_exercises) {

    private val binding by viewBinding(FragmentTopicExercisesBinding::bind)
    private lateinit var itemsAdapter: ItemsAdapter
    private var listener: OnItemClickListener? = null
    private var sentences: ArrayList<SentenceExercise>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnItemClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            binding.buttonCheckAnswers.visibility = View.VISIBLE
        }

        itemsAdapter = ItemsAdapter()
        binding.recyclerViewSentences.adapter = itemsAdapter

        arguments.let {
            sentences = it?.getParcelableArrayList(EXERCISES)
        }

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewSentences.layoutManager = layoutManager
        itemsAdapter.setList(sentences!!.toList())

        binding.buttonCheckAnswers.setOnClickListener {
            binding.buttonCheckAnswers.visibility = View.GONE
            matchValues(sentences!!)
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun matchValues(sentences: ArrayList<SentenceExercise>) {
        for (i in 0 until sentences.size) {
            val sentence = sentences[i]
            val viewHolder = binding.recyclerViewSentences.findViewHolderForAdapterPosition(i) as? GrammarTrainingViewHolder

            if (sentence.current_answer!!.isNullOrEmpty()) {
                viewHolder?.editText?.text = Editable.Factory.getInstance().newEditable(sentence.right_answer)
                viewHolder?.editText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                if (sentence.current_answer!! == sentence.right_answer) {
                    viewHolder?.editText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                } else {
                    viewHolder?.textView?.visibility = View.VISIBLE
                    viewHolder?.textView?.text = sentence.right_answer
                    viewHolder?.editText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
        }
    }

    companion object {
        private const val EXERCISES = "exercises"

        fun newInstance(exercises: ArrayList<SentenceExercise>): TopicExercisesFragment =
            TopicExercisesFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXERCISES, exercises)
                }
            }
    }
}
