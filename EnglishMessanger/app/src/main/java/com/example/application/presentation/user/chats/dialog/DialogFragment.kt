package com.example.application.presentation.user.chats.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.data.model.ChatMessage
import com.example.application.databinding.FragmentDialogBinding
import com.example.application.server.ApiClient
import com.example.application.server.WebSocketService
import kotlinx.coroutines.launch

class DialogFragment : Fragment(R.layout.fragment_dialog) {

    private val binding by viewBinding(FragmentDialogBinding::bind)
    private var recipientId: Long = 0
    private var senderId: Long = 0
    private lateinit var itemsAdapter: ItemsAdapter
    private var email: String = ""
    private var senderUsername: String = ""
    private var recipientUsername: String = ""
    private lateinit var webSocketService: WebSocketService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webSocketService = WebSocketService()

        arguments.let {
            email = it?.getString(EMAIL).toString()
        }

        generateDialog(email)

        binding.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imageViewSend.setOnClickListener {
            val chatMessage = ChatMessage(
                idChat =  generateChatId(senderId, recipientId).toString(),
                type = "CHAT",
                content = binding.editTextSend.text.toString(),
                sender = senderUsername,
                recipient = recipientUsername,
                senderId = senderId.toString(),
                recipientId = recipientId.toString()
            )
            binding.editTextSend.text = null
            webSocketService.connect(chatMessage)
            webSocketService.sendMessage(chatMessage)
            getChatMessages()
        }

        binding.imageViewSend.setOnLongClickListener {
            binding.imageViewSend.setImageResource(R.mipmap.icon_check_grammar_foreground)
            val message = binding.editTextSend.text.toString()

            binding.imageViewSend.setOnClickListener {
                checkGrammar(message)
                binding.imageViewSend.setImageResource(R.mipmap.icon_plane_foreground)
            }
            true
        }
    }

    private fun getChatMessages() {
        lifecycleScope.launch {
            try {
                val layoutManager = GridLayoutManager(requireContext(), 1)
                binding.recyclerViewMessages.layoutManager = layoutManager

                val user = ApiClient.service.getUserByEmail(email)
                senderId = user.id!!
                senderUsername = user.username!!

                val messages = ApiClient.service.getChatMessages(
                    generateChatId(
                        senderId,
                        recipientId
                    ).toString()
                )

                itemsAdapter = ItemsAdapter(senderUsername)

                binding.recyclerViewMessages.adapter = itemsAdapter
                itemsAdapter.setList(messages)
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun generateChatId(senderId: Long, recipientId: Long): Long {
        val ids = listOf(senderId, recipientId).sorted()
        return ids[0] + ids[1]
    }

    private fun generateDialog(email: String) {
        lifecycleScope.launch {
            try {
                val findUser = ApiClient.service.generateDialog(email)
                val topic = ApiClient.service.generateDialogTopic(email)

                binding.textViewUsername.text = findUser.username
                recipientId = findUser.id!!
                recipientUsername = findUser.username!!

                getChatMessages()
                Log.d("USER", recipientId.toString())
                val image = findUser.photo
                val imageUrl =
                    "https://s3.timeweb.cloud/c69f4719-fa278707-76a9-4ddc-bc9e-bc582ad152d2/${image}"

                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.mipmap.icon_camera_foreground)
                    .error(R.mipmap.icon_camera_foreground)
                    .into(binding.imageViewPhoto)

                binding.textViewTopic.text = topic.interest
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    private fun checkGrammar(message: String) {
        lifecycleScope.launch {
            try {
                val wordsWithPositions = mutableListOf<Pair<Int, String>>()
                var currentPosition = 0

                message.split(" ").forEach { word ->
                    val wordStartPosition = message.indexOf(word, currentPosition)
                    if (wordStartPosition != -1) {
                        wordsWithPositions.add(wordStartPosition to word)
                        currentPosition = wordStartPosition + word.length
                    }
                }

                var words = wordsWithPositions.toList()

                val correction = ApiClient.service.checkGrammar(message)

                correction.forEach { match ->
                    match.replacements?.firstOrNull()?.let { replacement ->
                        replacement.value?.let { newValue ->
                            val wordIndex = match.offset

                            words = words.map { (startIndex, word) ->
                                if (startIndex == wordIndex) {
                                    wordIndex to newValue
                                } else {
                                    startIndex to word
                                }
                            }
                        }
                    }
                }

                val newText = words.joinToString(separator = " ") { it.second }
                binding.editTextSend.setText(newText)
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data: ${e.message}")
            }
        }
    }

    companion object {
        private const val EMAIL = "email"

        fun newInstance(email: String): DialogFragment {
            return DialogFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAIL, email)
                }
            }
        }
    }
}