package com.example.telegramm.data.service

import com.example.telegramm.data.model.ChatResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChatService {
    @GET("chat/{chatId}")
    suspend fun getChat(
        @Path("chatId") chatId: Int
    ): List<ChatResponse>

    @FormUrlEncoded
    @POST("chat/send")
    suspend fun sendMessage(
        @Field("chatId") chatId: Int?= 1,
        @Field("message") message: String,
        @Field("receiverId") receiverId: String? = "1",
        @Field("senderId") senderId: String? = "2"
    ): ChatResponse

    @DELETE("chat/{chatId}/message/{messageId}")
    suspend fun deleteMessage(
        @Path("chatId") chatId: Int,
        @Path("messageId") messageId: Int
    ): Unit

    @FormUrlEncoded
    @PUT("chat/{chatId}/message/{messageId}")
    suspend fun updateMessage(
        @Path("chatId") chatId: Int,
        @Path("messageId") messageId: Int,
        @Field("newMessage") newMessage: String
    ): ChatResponse
}