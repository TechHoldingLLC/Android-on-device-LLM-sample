package com.app.ondevicellmdemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.ondevicellmdemo.R

@Composable
fun ChatView(
    chatViewModel: ChatViewModel,
) {
    val llmState = chatViewModel.llmState.collectAsStateWithLifecycle()
    val chatState = chatViewModel.chatState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        chatViewModel.initLLMModel()
    }
    Surface {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) { innerPadding ->
            if (llmState.value.isLLMModelLoading) {
                LoadingView()
            } else {
                ChatContent(
                    chatState.value.chatMessages,
                    llmState.value.isLLMResponseLoading,
                    chatViewModel::sendMessage,
                    innerPadding
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Loading model...")
        }
    }
}

@Composable
fun ChatContent(
    chatMessages: List<ChatDataModel>,
    isLLMResponseLoading: Boolean,
    onSendClick: (message: String) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(chatMessages) { chatDataModel ->
                ChatMessageView(chatDataModel)
            }
        }
        ChatInputView(
            isLoading = isLLMResponseLoading,
            onSendClick = onSendClick
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun ChatInputView(
    isLoading: Boolean,
    onSendClick: (message: String) -> Unit,
) {
    var chatMessage by remember { mutableStateOf("") }
    val sendClick by rememberUpdatedState(onSendClick)

    ThemedTextFiledWithIcons(
        value = chatMessage,
        onValueChange = { chatMessage = it },
        singleLine = false,
        hintText = "Type a message",
        trailingIcon = {
            if (isLoading) {
                Box(
                    modifier = Modifier.size(24.dp)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        if (chatMessage.isEmpty()) return@IconButton
                        sendClick(chatMessage)
                        chatMessage = ""
                    },
                ) {
                    ThemedCustomIcon(
                        painterResource(id = R.drawable.ic_send),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ChatMessageView(chatDataModel: ChatDataModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (chatDataModel.isUser) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Column(
            horizontalAlignment = if (chatDataModel.isUser) {
                Alignment.End
            } else {
                Alignment.Start
            }
        ) {
            Text(
                text = if (chatDataModel.isUser) "You" else "Model",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = chatDataModel.chatMessage,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = if (chatDataModel.isUser) 12.dp else 0.dp,
                            topEnd = if (chatDataModel.isUser) 0.dp else 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
                    .background(
                        if (chatDataModel.isUser) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        }
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            )
        }
    }
}
