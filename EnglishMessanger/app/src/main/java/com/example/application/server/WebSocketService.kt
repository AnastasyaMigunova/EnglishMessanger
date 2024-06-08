package com.example.application.server

import android.annotation.SuppressLint
import android.util.Log
import com.example.application.data.model.ChatMessage
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class WebSocketService {
    private val url = "ws://90.156.224.51:8081/ws"
    private lateinit var stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()

    fun connect(chatMessage: ChatMessage) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
        stompClient.connect()

        val lifecycleDisposable = stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("WebSocket", "Connected")
                    subscribeToTopic(chatMessage.idChat.toString())
                }

                LifecycleEvent.Type.ERROR -> {
                    Log.e("WebSocket", "Error: ${lifecycleEvent.exception?.message}")
                }

                LifecycleEvent.Type.CLOSED -> {
                    Log.d("WebSocket", "Disconnected")
                }
                else -> {}
            }
        }
        compositeDisposable.add(lifecycleDisposable)
    }

    @SuppressLint("CheckResult")
    fun sendMessage(chatMessage: ChatMessage) {
        try {
            val sendDisposable = stompClient.send(
                "/app/chat.sendMessage/${chatMessage.idChat}",
                Gson().toJson(chatMessage)
            ).subscribe(
                { Log.d("WEBSOCKET_SEND_MESSAGE", "Message sent") },
                { e -> Log.e("WEBSOCKET_SEND_MESSAGE", "Error: ${e.message}") }
            )
            compositeDisposable.add(sendDisposable)
        } catch (e: Exception) {
            Log.e("WEBSOCKET_SEND_MESSAGE", "Error encoding chat message: ${e.message}")
        }
    }

    fun addUserToChat(chatMessage: ChatMessage) {
        try {
            val sendDisposable = stompClient.send(
                "/app/chat.addUser/${chatMessage.idChat}",
                Gson().toJson(chatMessage)
            ).subscribe(
                { Log.d("WEBSOCKET_ADD_USER", "User added") },
                { e -> Log.e("WEBSOCKET_ADD_USER", "Error: ${e.message}") }
            )
            compositeDisposable.add(sendDisposable)
        } catch (e: Exception) {
            Log.e("WEBSOCKET_ADD_USER", "Error encoding chat message: ${e.message}")
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeToTopic(chatId: String) {
        val topicDisposable = stompClient.topic("/topic/public/$chatId").subscribe { topicMessage ->
            Log.d("WEBSOCKET_SUBSCRIBE", topicMessage.payload)
        }
        compositeDisposable.add(topicDisposable)
    }

    fun disconnect() {
        if (::stompClient.isInitialized && stompClient.isConnected) {
            stompClient.disconnect()
        }
        compositeDisposable.clear()
    }
}