package com.example.telegramm.data.repository

import com.example.telegramm.data.core.Either
import com.example.telegramm.data.core.RetrofitClient
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.data.service.ChatService


class ChatRepository {
    private val chatService: ChatService = RetrofitClient.chatService

    suspend fun getChat(chatId: Int): Either<Throwable, List<ChatResponse>> =
        try {
            val response = RetrofitClient.chatService.getChat(chatId)
            Either.Success(response)
        } catch (e: Exception) {
            Either.Error(e)
        }

    suspend fun sendMessage(message: String): Either<Throwable, ChatResponse> =
        try {
            val response = chatService.sendMessage(message = message)
            Either.Success(response)
        } catch (e: Exception) {
            Either.Error(e)
        }

    suspend fun deleteMessage(chatId: Int, messageId: Int) =
        chatService.deleteMessage(chatId, messageId)

    suspend fun updateMessage(chatId: Int, messageId: Int, newMessage: String) =
        chatService.updateMessage(chatId, messageId, newMessage)
}