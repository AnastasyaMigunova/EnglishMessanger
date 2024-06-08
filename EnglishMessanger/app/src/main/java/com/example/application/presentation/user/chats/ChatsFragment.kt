package com.example.application.presentation.user.chats

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.data.model.CardSet
import com.example.application.databinding.FragmentChatsBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.`interface`.OnItemClickListener
import com.example.application.presentation.user.chats.dialog.DialogFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val binding by viewBinding(FragmentChatsBinding::bind)
    private var requestedEmail: String = ""
    private lateinit var itemsAdapter: ItemsAdapter
    private var buttonListener: OnButtonClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")

        binding.itemFriend.cardViewFriend.visibility = View.GONE
        binding.buttonFindCompanion.visibility = View.VISIBLE
        binding.recyclerViewFriends.visibility = View.VISIBLE

        email?.let { fetchAllChatUsers(it) }

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerViewFriends.layoutManager = layoutManager

        binding.editTextSearch.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                binding.recyclerViewFriends.visibility = View.GONE

                val username = "@" + binding.editTextSearch.text.toString()
                getUserByUsername(username)
                binding.editTextSearch.text = null
                return@setOnKeyListener true
            }
            false
        }

        binding.buttonFindCompanion.setOnClickListener {
            buttonListener?.onButtonCLick(DialogFragment.newInstance(email!!), "DIALOG")
        }
    }

    private fun getUserByUsername(username: String) {
        lifecycleScope.launch {
            try {
                val user = ApiClient.service.getUserByUsername(username)

                binding.itemFriend.cardViewFriend.visibility = View.VISIBLE
                binding.recyclerViewFriends.visibility = View.GONE
                binding.itemFriend.imageViewChat.visibility = View.GONE

                val image = user.photo
                val imageUrl =
                    "https://s3.timeweb.cloud/c69f4719-fa278707-76a9-4ddc-bc9e-bc582ad152d2/${image}"

                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.mipmap.icon_camera_foreground)
                    .error(R.mipmap.icon_camera_foreground)
                    .into(binding.itemFriend.imageViewPhoto)

                binding.itemFriend.textViewName.text = buildString {
                    append(user.firstName)
                    append(" ")
                    append(user.lastName)
                }
                binding.itemFriend.textViewDateLevel.text = username
                binding.itemFriend.imageViewChat.setImageResource(R.mipmap.icon_add_symbol_foreground)
                requestedEmail = user.email
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun fetchAllChatUsers(email: String) {
        lifecycleScope.launch {
            try {
                val friends = ApiClient.service.fetchAllChatUsers(email)
                itemsAdapter = ItemsAdapter()

                binding.recyclerViewFriends.adapter = itemsAdapter.apply {
                    listener = object : OnItemClickListener {
                        override fun onItemClick(position: Int) {}
                        override fun onItemSetClick(cardSet: CardSet) {}
                        override fun onItemExerciseClick(rightAnswer: String, currentAnswer: String) {
                        }
                    }
                }
                itemsAdapter.setList(friends)

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance() : ChatsFragment {
            return ChatsFragment()
        }
    }
}