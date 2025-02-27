package com.example.telegramm.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.telegramm.data.model.ChatResponse
import com.example.telegramm.databinding.FragmentChatBinding
import com.example.telegramm.ui.adapter.ChatAdapter
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding
    private var adapter = ChatAdapter(
        clickListener = {message -> showUpdateDialog(message)},
        longClickListener = {message -> onLongClick(message)}
    )

    private fun showUpdateDialog(message: ChatResponse) {
        val builder = AlertDialog.Builder(requireContext())
        val editText = EditText(requireContext())
        editText.setText(message.message)
        builder.setView(editText)
        builder.setPositiveButton("Update"){ _, _ ->
            val text = editText.text.toString().trim()
            if (text.isNotEmpty()){
//                viewModel.updateMessage(message.id, text)
            }
        }
        builder.setNegativeButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun onLongClick(message: ChatResponse) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete message")
        builder.setMessage("Are you sure you want to delete this message")
        builder.setPositiveButton("Delete") {_, _ ->
//            viewModel.deleteMessage(message.id)
        }
        builder.setNegativeButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
//        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun init() {

    }

    private fun setListeners() {
        binding.recycler.adapter = adapter
        //viewModel.getChat(1)

        // Обработчик кнопки отправки
        binding.btn.setOnClickListener {
            val message = binding.editText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.editText.text.clear() // Очищаем поле ввода
                viewModel.getChat(1) // Обновляем чат после отправки
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(lifecycle.currentState) {
                viewModel.event.observe(viewLifecycleOwner) { event ->
                    when(event){
                        is ChatViewModel.UiEvent.ShowError -> showToast(event.message)
                        is ChatViewModel.UiEvent.SendMessage -> showToast(event.message)
                        is ChatViewModel.UiEvent.DeleteMessage -> showToast(event.message)
                        is ChatViewModel.UiEvent.UpdateMessage -> showToast(event.message)
                    }
                }
                viewModel.message.observe(viewLifecycleOwner){
                    adapter.submitList(it.sortedBy { it.timestamp }) // Обновляем адаптер после получения новых сообщений
                    binding.recycler.scrollToPosition(adapter.itemCount - 1) // Перемещаемся в конец списка после получения новых сообщений
                }
            }

        }

    }

    private fun showToast(message: String) {
        Log.e("ololo", "showToast: $message", )
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }


}