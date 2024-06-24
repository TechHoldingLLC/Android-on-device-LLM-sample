package com.app.ondevicellmdemo.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ondevicellmdemo.llm.LLMTask
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    // State of the UI
    private val _llmState = MutableStateFlow<LLMState>(LLMState.LLMModelLoading)
    val llmState = _llmState.asStateFlow()
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> =
        _chatState.asStateFlow()

    fun initLLMModel() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _llmState.emit(LLMState.LLMModelLoading)
                LLMTask.getInstance(context)
                _llmState.emit(LLMState.LLMModelLoaded)
            } catch (e: Exception) {
                _llmState.emit(LLMState.LLMModelFailedToLoad(e.localizedMessage ?: "Unknown Error"))
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _chatState.value.appendUserMessage(message)
            try {
                _llmState.emit(LLMState.LLMResponseLoading)
                var currentLLMResponseId: String? = _chatState.value.createLLMLoadingMessage()
                LLMTask.getInstance(context).generateResponse(_chatState.value.fullPrompt)
                LLMTask.getInstance(context).partialResults?.collectIndexed { index, (partialResult, done) ->
                    currentLLMResponseId?.let { id ->
                        if (index == 0) {
                            _chatState.value.appendFirstLLMResponse(
                                id,
                                partialResult
                            )
                        } else {
                            _chatState.value.appendLLMResponse(
                                id,
                                partialResult,
                                done
                            )
                        }
                        if (done) {
                            _llmState.emit(LLMState.LLMResponseLoaded)
                            currentLLMResponseId = null
                        }
                    }
                }

            } catch (e: Exception) {
                _chatState.value.addErrorLLMResponse(e)
            }
        }
    }
}


