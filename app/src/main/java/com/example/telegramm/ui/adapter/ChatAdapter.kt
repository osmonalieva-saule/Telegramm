package com.example.telegramm.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.databinding.ChatItemBinding


class ChatAdapter(
    private val clickListener: (ChatResponse) -> Unit,
    private val longClickListener: (ChatResponse) -> Unit
): ListAdapter<ChatResponse, ChatAdapter.ChatViewHolder>(DifUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)
        with(holder.binding) {
            txt.text = message.message
        }
    }

    class DifUtil : DiffUtil.ItemCallback<ChatResponse>(){
        override fun areItemsTheSame(
            oldItem: ChatResponse,
            newItem: ChatResponse
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ChatResponse,
            newItem: ChatResponse
        ): Boolean{
            return oldItem == newItem
        }
    }
    class ChatViewHolder(val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root)
}