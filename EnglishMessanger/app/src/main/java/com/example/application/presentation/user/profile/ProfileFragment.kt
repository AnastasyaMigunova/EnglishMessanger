package com.example.application.presentation.user.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.databinding.FragmentProfileBinding
import com.example.application.`interface`.OnButtonClickListener
import com.example.application.presentation.StartFragment
import com.example.application.presentation.user.profile.friends.FriendsFragment
import com.example.application.server.ApiClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private var listener: OnButtonClickListener? = null
    private var username: String = ""
    private val friendsFragment = FriendsFragment.newInstance()
    private var countFriends: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as? OnButtonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        username = sharedPreferences.getString("username", "").toString()


        if (username.isEmpty() && email != null) {
            lifecycleScope.launch {
                username = getUsername(email)
                getUserByUsername(username, email)
            }
        } else {
            email?.let { getUserByUsername(username, it) }
        }

        binding.textViewSignOut.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        binding.cardViewFriends.cardViewItem.setOnClickListener {
            listener?.onButtonCLick(friendsFragment, "FRIENDS")
        }

        binding.textViewSignOut.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            listener?.onButtonCLick(StartFragment.newInstance(), null.toString())
        }
    }

    private suspend fun getUsername(email: String): String {
        return try {
            val user = ApiClient.service.getUserByEmail(email)
            user.username.toString()
        } catch (e: Exception) {
            Log.e("ApiService", "Error fetching data (getUsername): ${e.message}")
            ""
        }
    }

    private fun getUserByUsername(username: String, email: String) {
        lifecycleScope.launch {
            try {
                val user = ApiClient.service.getUserByUsername(username)

                val image = user.photo

                val imageUrl =
                    "https://s3.timeweb.cloud/c69f4719-fa278707-76a9-4ddc-bc9e-bc582ad152d2/${image}"

                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.mipmap.icon_camera_foreground)
                    .error(R.mipmap.icon_camera_foreground)
                    .into(binding.imageViewAvatar)

                binding.textViewName.text =
                    buildString {
                        append(user.firstName)
                        append(" ")
                        append(user.lastName)
                    }

                binding.textViewUsername.text = user.username

                val formattedDate = user.dateOfBirth?.let { formatDate(it) }
                binding.cardViewBirthday.textViewItem.text = buildString {
                    append("День Рождения: ")
                    append(formattedDate)
                }

                binding.cardViewLevel.textViewItem.text = buildString {
                    append("Уровень языка: ")
                    append(user.languageLevel)
                }

                getFriends(email)

                binding.cardViewFriends.buttonNext.visibility = View.VISIBLE

                binding.cardViewInterests.textViewItem.text = "Интересы"
                binding.cardViewInterests.buttonNext.visibility = View.VISIBLE

            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data (getUserByUsername): ${e.message}")
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error parsing date: ${e.message}")
            ""
        }
    }

    private fun getFriends(email: String) {
        lifecycleScope.launch {
            try {
                countFriends = ApiClient.service.getFriends(email).size

                binding.cardViewFriends.textViewItem.text = buildString {
                    append("Друзья: ")
                    append(countFriends)
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error fetching data (getFriends): ${e.message}")
            }
        }
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}