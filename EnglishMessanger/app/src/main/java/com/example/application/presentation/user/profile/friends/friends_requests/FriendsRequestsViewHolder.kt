package com.example.application.presentation.user.profile.friends.friends_requests

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.data.model.User
import com.example.application.databinding.ItemFriendRequestBinding
import com.example.application.`interface`.OnFriendRequestListener
import java.text.SimpleDateFormat
import java.util.Locale

class FriendsRequestsViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val listener: OnFriendRequestListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_friend_request, parent, false),
) {
    private val binding by viewBinding(ItemFriendRequestBinding::bind)

    fun bind(user: User, position: Int) = with(binding) {
        val image = user.photo
        val imageUrl =
            "https://s3.timeweb.cloud/c69f4719-fa278707-76a9-4ddc-bc9e-bc582ad152d2/${image}"

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.mipmap.icon_camera_foreground)
            .error(R.mipmap.icon_camera_foreground)
            .into(binding.imageViewPhoto)

        binding.textViewName.text = buildString {
            append(user.firstName)
            append(" ")
            append(user.lastName)
        }

        val formattedDate = user.dateOfBirth?.let { formatDate(it) }
        binding.textViewDateLevel.text = buildString {
            append(formattedDate)
            append(", ")
            append(user.languageLevel)
        }

        binding.imageViewAccepted.setOnClickListener {
            listener.onFriendRequestCLick(true, user.email)
        }

        binding.imageViewRejection.setOnClickListener {
            listener.onFriendRequestCLick(false, user.email)
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
}