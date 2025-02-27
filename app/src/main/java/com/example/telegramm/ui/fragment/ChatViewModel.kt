package com.example.telegramm.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telegramm.data.core.Either
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val _message = MutableLiveData<List<ChatResponse>>()
    val message: LiveData<List<ChatResponse>> get() = _message

    private val _event = MutableLiveData<UiEvent>()
    val event: LiveData<UiEvent> get() = _event

    init {
        getChat(1)
    }

    fun getChat(chatId: Int){
        viewModelScope.launch {
            when (val result = repository.getChat(chatId)) {
                is Either.Success -> _message.postValue(result.data)
                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка загрузки чата: ${result.error.message}"))
            }
        }
    }

    private suspend fun sendEvent(event: UiEvent) {
        _event.postValue(event)
    }

    fun sendMessage( message: String) {
        viewModelScope.launch {
            when (val result = repository.sendMessage( message)) {
                is Either.Success -> {
                    val updatedList = _message.value.orEmpty() + result.data
                    _message.postValue(updatedList)
                    sendEvent(UiEvent.SendMessage("Сообщение отправлено"))
                }
                is Either.Error -> sendEvent(UiEvent.ShowError("Ошибка отправки: ${result.error.message}"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
        data class SendMessage(val message: String) : UiEvent()
        data class DeleteMessage(val message: String) : UiEvent()
        data class UpdateMessage(val message: String) : UiEvent()
    }
}