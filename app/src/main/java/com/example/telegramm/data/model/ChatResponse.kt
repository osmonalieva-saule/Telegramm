package com.example.telegramm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    @SerialName("chatId")
    var chatId: Int? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("message")
    var message: String? = null,
    @SerialName("receiverId")
    var receiverId: String? = null,
    @SerialName("senderId")
    var senderId: String? = null,
    @SerialName("timestamp")
    var timestamp: String? = null
)