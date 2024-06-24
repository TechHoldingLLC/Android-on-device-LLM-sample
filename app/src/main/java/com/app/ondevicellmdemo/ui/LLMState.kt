package com.app.ondevicellmdemo.ui

import androidx.compose.runtime.toMutableStateList


sealed class LLMState {
    data object LLMModelLoading : LLMState()
    data object LLMModelLoaded : LLMState()
    data object LLMResponseLoading : LLMState()
    data object LLMResponseLoaded : LLMState()

    // Make val to check the state
    val isLLMModelLoading get() = this is LLMModelLoading
    val isLLMResponseLoading get() = this is LLMResponseLoading
}

class ChatState(
    messages: List<ChatDataModel> = emptyList()
) {
    private val _chatMessages: MutableList<ChatDataModel> = messages.toMutableStateList()
    val chatMessages: List<ChatDataModel>
        get() = _chatMessages.map { model ->
            val isUser = model.isUser
            val prefixToRemove =
                if (isUser) USER_PREFIX else MODEL_PREFIX
            model.copy(
                chatMessage = model.chatMessage
                    .replace(
                        START_TURN + prefixToRemove + "\n",
                        ""
                    )
                    .replace(
                        END_TURN,
                        ""
                    )
            )
        }.reversed()

    val fullPrompt
        get() =
            _chatMessages.takeLast(5).joinToString("\n") { it.chatMessage }

    fun createLLMLoadingMessage(): String {
        val chatMessage = ChatDataModel(
            chatMessage = "",
            isUser = false
        )
        _chatMessages.add(chatMessage)
        return chatMessage.id
    }

    fun appendFirstLLMResponse(
        id: String,
        message: String,
    ) {
        appendLLMResponse(
            id,
            "$START_TURN$MODEL_PREFIX\n$message",
            false
        )
    }


    fun appendLLMResponse(
        id: String,
        message: String,
        done: Boolean
    ) {
        val index = _chatMessages.indexOfFirst { it.id == id }
        if (index != -1) {
            val newText = if (done) {
                _chatMessages[index].chatMessage + message + END_TURN
            } else {
                _chatMessages[index].chatMessage + message
            }
            _chatMessages[index] = _chatMessages[index].copy(chatMessage = newText)
        }
    }

    fun appendUserMessage(
        message: String,
    ) {
        val chatMessage = ChatDataModel(
            chatMessage = "$START_TURN$USER_PREFIX\n$message$END_TURN",
            isUser = true
        )
        _chatMessages.add(chatMessage)
    }

    fun addErrorLLMResponse(e: Exception) {
        _chatMessages.add(
            ChatDataModel(
                chatMessage = e.localizedMessage ?: "Error generating message",
                isUser = false
            )
        )
    }

    companion object {
        private const val MODEL_PREFIX = "model"
        private const val USER_PREFIX = "user"
        private const val START_TURN = "<start_of_turn>"
        private const val END_TURN = "<end_of_turn>"
    }
}

