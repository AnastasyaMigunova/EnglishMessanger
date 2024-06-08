package com.example.application.presentation.onboarding.testing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.data.model.Answer
import com.example.application.databinding.FragmentResultTestBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.onboarding.interests.InterestsViewModel
import com.example.application.presentation.user.MainProfileFragment
import com.example.application.presentation.user.profile.ProfileFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class ResultTestFragment : Fragment(R.layout.fragment_result_test) {

    private val binding by viewBinding(FragmentResultTestBinding::bind)
    private val fragment = MainProfileFragment.newInstance()
    private lateinit var answerViewModel: AnswersViewModel
    private lateinit var interestsViewModel: InterestsViewModel
    private var listener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        answerViewModel = ViewModelProvider(requireActivity())[AnswersViewModel::class.java]
        interestsViewModel = ViewModelProvider(requireActivity())[InterestsViewModel::class.java]

        getCurrentLevel()
        saveUserInterests()

        binding.buttonGetStarted.setOnClickListener {
            listener?.onButtonCLick(fragment, "MAIN_PROFILE")
        }
    }

    private fun getCurrentLevel() {
        lifecycleScope.launch {
            try {
                val sharedPreferences =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val email = sharedPreferences.getString("email", "")

                val answerList: List<Answer>? = answerViewModel.getData().value

                val level = email?.let {
                    answerList?.let { list ->
                        ApiClient.service.getCurrentLevel(
                            it,
                            list
                        )
                    }
                }?.body()

                binding.textViewResultTest.text = level.toString()
                editor.putString("level", binding.textViewResultTest.text.toString())
                editor.apply()

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun saveUserInterests() {
        lifecycleScope.launch {
            try {
                val sharedPreferences =
                    requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val email = sharedPreferences.getString("email", "")

                val interests = interestsViewModel.getData().value
                val interestIds = interests?.map { it.id.toLong() }
                val interestsAnswer = email?.let { interestIds?.let { ids ->
                    ApiClient.service.saveUserInterests(it,
                        ids
                    )
                } }
                Log.d("INTERESTS", interestsAnswer.toString())

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): ResultTestFragment {
            return ResultTestFragment()
        }
    }
}