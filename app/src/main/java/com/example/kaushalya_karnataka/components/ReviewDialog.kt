package com.example.kaushalya_karnataka.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Int, String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var text by remember { mutableStateOf("") }
    val haptics = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How was the service?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Your feedback builds the local trust economy.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Interactive Stars with Haptics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 1..5) {
                    val isActive = i <= rating
                    Icon(
                        imageVector = if (isActive) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Rate $i stars",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { 
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                rating = i 
                            },
                        tint = if (isActive) Color(0xFFEAB308) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    if (i < 5) Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Write a short review (optional)") },
                modifier = Modifier.fillMaxWidth().height(140.dp),
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text("Quality of work, punctuality, pricing...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { 
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSubmit(rating, text) 
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 4.dp)
            ) {
                Text(
                    text = "Post Review", 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
