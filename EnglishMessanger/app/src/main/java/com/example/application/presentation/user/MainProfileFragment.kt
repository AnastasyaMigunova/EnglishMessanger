package com.example.application.presentation.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentMainProfileBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.user.chats.ChatsFragment
import com.example.application.presentation.user.dictionary.DictionaryFragment
import com.example.application.presentation.user.main_exercises.MainExercisesFragment
import com.example.application.presentation.user.profile.ProfileFragment

class MainProfileFragment : Fragment(R.layout.fragment_main_profile) {

    private val binding by viewBinding(FragmentMainProfileBinding::bind)
    private var listener: OnButtonClickListener? = null
    private val chatsFragment = ChatsFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()
    private val dictionaryFragment = DictionaryFragment.newInstance()
    private val exercisesFragment = MainExercisesFragment.newInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chats -> {
                    setFragment(chatsFragment, "CHATS")
                    true
                }

                R.id.home -> {
                    setFragment(profileFragment, "PROFILE")
                    true
                }

                R.id.dictionary -> {
                    setFragment(dictionaryFragment, "DICTIONARY")
                    true
                }

                R.id.exercises -> {
                    setFragment(exercisesFragment, "MAIN_EXERCISES")
                    true
                }

                else -> false
            }
        }
    }

    private fun setFragment(fragment: Fragment, name: String) {
        childFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(name)
            .commit()
    }

    companion object {
        fun newInstance() : MainProfileFragment {
            return MainProfileFragment()
        }
    }
}