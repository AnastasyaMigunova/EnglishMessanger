package com.example.application.presentation.user.profile.friends.friends_requests

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.application.R
import com.example.application.databinding.FragmentFriendsRequestsBinding
import com.example.application.`interface`.OnFriendRequestListener
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class FriendsRequestsFragment : Fragment(R.layout.fragment_friends_requests) {

    private val binding by viewBinding(FragmentFriendsRequestsBinding::bind)
    private lateinit var itemsAdapter: ItemsAdapter
    private var email: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", "").toString()

        getFriendsRequests(email)
    }

    private fun getFriendsRequests(email: String) {
        lifecycleScope.launch {
            try {
                val friendsRequests = ApiClient.service.getFriendsRequests(email)

                if (friendsRequests.isEmpty()) {
                    binding.textViewNoRequests.visibility = View.VISIBLE
                    binding.recyclerViewFriendsRequests.visibility = View.GONE
                } else {
                    binding.textViewNoRequests.visibility = View.GONE
                    binding.recyclerViewFriendsRequests.visibility = View.VISIBLE

                    val layoutManager = GridLayoutManager(requireContext(), 1)
                    binding.recyclerViewFriendsRequests.layoutManager = layoutManager

                    itemsAdapter = ItemsAdapter()

                    binding.recyclerViewFriendsRequests.adapter = itemsAdapter.apply {
                        listener = object : OnFriendRequestListener {
                            override fun onFriendRequestCLick(flag: Boolean, sentEmail: String) {
                                if (flag) {
                                    acceptedFriending(email, sentEmail)
                                    itemsAdapter.setList(friendsRequests)
                                } else {
                                    rejectedFriending(email, sentEmail)
                                    itemsAdapter.setList(friendsRequests)
                                }
                            }
                        }
                    }
                    itemsAdapter.setList(friendsRequests)
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun acceptedFriending(email: String, sentEmail: String) {
        lifecycleScope.launch {
            try {
                val answer = ApiClient.service.acceptedFriending(email, sentEmail)
                Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Запрос на добавление в друзья уже существует.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun rejectedFriending(email: String, sentEmail: String) {
        lifecycleScope.launch {
            try {
                val answer = ApiClient.service.rejectedFriending(email, sentEmail)
                Toast.makeText(requireContext(), answer, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Запрос на добавление в друзья уже существует.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): FriendsRequestsFragment {
            return FriendsRequestsFragment()
        }
    }
}