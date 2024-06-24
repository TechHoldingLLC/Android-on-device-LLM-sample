package com.app.ondevicellmdemo.ui

import java.util.UUID

data class ChatDataModel(
    val id: String = UUID.randomUUID().toString(),
    val chatMessage: String,
    val isUser: Boolean
)
