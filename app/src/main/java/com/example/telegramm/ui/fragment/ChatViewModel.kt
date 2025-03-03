package com.example.telegramm.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telegramm.data.core.Either
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _messages = MutableLiveData<List<ChatResponse>>()
    val message: LiveData<List<ChatResponse>> get() = _messages

    private val _event = MutableLiveData<UiEvent>()
    val event: LiveData<UiEvent> get() = _event

    init {
        getChat(1)
    }

    fun getChat(chatId: Int){
        viewModelScope.launch {
            when (val result = repository.getChat(chatId)) {
                is Either.Success -> _messages.postValue(result.data)

                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка загрузки чата: ${result.error.message}"))
            }
        }
    }


    private fun sendEvent(event: UiEvent) {
        _event.value = event
    }

    fun sendMessage(chatId: Int, message: String, receiverId: String, senderId: String) {
        viewModelScope.launch {
            when (val result = repository.sendMessage(chatId, message, receiverId, senderId)) {
                is Either.Success -> {
                    val updatedList = _messages.value.orEmpty() + result.data
                    _messages.postValue(updatedList)
                    sendEvent(UiEvent.SendMessage("Сообщение отправлено"))
                }
                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка отправки: ${result.error.message}"))
            }
        }
    }
    fun deleteMessage(chatId: Int, messageId: Int) {
        viewModelScope.launch {
            when (val result = repository.deleteMessage(chatId, messageId)) {
                is Either.Success -> sendEvent(UiEvent.DeleteMessage("Сообщение удалено"))
                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка удаления: ${result.error.message}"))
            }
        }
    }

    fun updateMessage(chatId: Int, messageld: Int, newMessage: String){
        viewModelScope.launch {
            when (val result = repository.updateMessage(chatId, messageld, newMessage)) {
                is Either.Success -> sendEvent(UiEvent.UpdateMessage("Сообщение изменено"))
                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка изменения: ${result.error.message}"))
            }
        }
    }
    fun refreshChat(chatId: Int) {
        getChat(chatId)
    }

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class SendMessage(val message: String) : UiEvent()
        data class DeleteMessage(val message: String) : UiEvent()
        data class UpdateMessage(val message: String) : UiEvent()
    }
}