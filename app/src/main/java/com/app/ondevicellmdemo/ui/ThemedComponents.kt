package com.app.ondevicellmdemo.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ThemedTextFiledWithIcons(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hintText,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.surface,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            errorCursorColor = MaterialTheme.colorScheme.onPrimary,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.secondary,
                backgroundColor = MaterialTheme.colorScheme.secondary,
            ),
        ),
        modifier = modifier,
        singleLine = singleLine,
        trailingIcon = {
            trailingIcon?.invoke()
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
fun ThemedCustomIcon(
    icon: Any,
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    when (icon) {
        is ImageVector -> Icon(
            icon,
            contentDescription = null,
            tint = tint ?: Color.Unspecified,
            modifier = modifier
        )

        is Painter -> Icon(
            icon,
            contentDescription = null,
            tint = tint ?: Color.Unspecified,
            modifier = modifier
        )
    }
}

