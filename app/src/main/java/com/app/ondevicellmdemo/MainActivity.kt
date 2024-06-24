package com.app.ondevicellmdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.app.ondevicellmdemo.ui.ChatView
import com.app.ondevicellmdemo.ui.ChatViewModel
import com.app.ondevicellmdemo.ui.theme.OnDeviceLLMDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val chatViewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnDeviceLLMDemoTheme {
                ChatView(
                    chatViewModel = chatViewModel,
                )

            }
        }
    }
}
