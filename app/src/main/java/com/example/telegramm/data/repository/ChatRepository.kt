package com.example.telegramm.data.repository

import android.util.Log
import com.example.telegramm.data.core.Either
import com.example.telegramm.data.core.RetrofitClient
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.data.service.ChatService
import javax.inject.Inject


class ChatRepository @Inject constructor(
    private val chatService: ChatService
){


    suspend fun getChat(chatId: Int): Either<Throwable, List<ChatResponse>> =
        try {
            val response = chatService.getChat(chatId)
            Either.Success(response)
        } catch (e: Exception) {
            e.message?.let { Log.e("OLOLO" , it) }
            Either.Error(e)
        }

    suspend fun sendMessage(chatId: Int, message: String, receiverId: String, senderId: String): Either<Throwable, ChatResponse> =
    try {
       val response = chatService.sendMessage(chatId, message, receiverId, senderId)
        Either.Success(response )
    } catch (e: Exception) {
        Either.Error(e)
    }


    suspend fun deleteMessage(chatId: Int, messageId: Int): Either<Throwable, Unit> =
        try {
            val response = chatService.deleteMessage(chatId, messageId)
            Either.Success(response)
        } catch (e: Exception) {
            e.message?.let { Log.e("OLOLO" , it )}
            Either.Error(e)
        }



    suspend fun updateMessage(chatId: Int, messageId: Int, newMessage: String): Either<Throwable, ChatResponse> =
        try {
          val response = chatService.updateMessage(chatId, messageId, newMessage)
            Either.Success(response)
        } catch (e: Exception) {
            e.message?.let { Log.e("LOLOL" , it)}
            Either.Error(e)
        }

}