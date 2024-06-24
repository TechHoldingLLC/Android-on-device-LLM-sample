package com.app.ondevicellmdemo.llm

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LLMTask(context: Context) {
    private val _partialResults = MutableSharedFlow<Pair<String, Boolean>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val partialResults: SharedFlow<Pair<String, Boolean>> = _partialResults.asSharedFlow()
    private var llmInference: LlmInference

    init {
        val options = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(MODEL_PATH)
            .setMaxTokens(2048)
            .setTopK(50)
            .setTemperature(0.7f)
            .setRandomSeed(1)
            .setResultListener { partialResult, done ->
                _partialResults.tryEmit(partialResult to done)
            }
            .build()

        llmInference = LlmInference.createFromOptions(
            context,
            options
        )
    }

    fun generateResponse(prompt: String) {
        llmInference.generateResponseAsync(prompt)
    }

    companion object {
        private const val MODEL_PATH = "/data/local/tmp/llm/gemma2b.bin"
        private var instance: LLMTask? = null
        fun getInstance(context: Context): LLMTask {
            return if (instance != null) {
                instance!!
            } else {
                LLMTask(context).also { instance = it }
            }
        }
    }
}
