package com.example.kaushalya_karnataka.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.viewmodel.SplashDestination
import com.example.kaushalya_karnataka.viewmodel.SplashViewModel
import com.example.kaushalya_karnataka.viewmodel.SplashViewModelFactory

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDiscovery: () -> Unit,
    onNavigateToProfileSetup: () -> Unit,
    repository: WorkerRepository
) {
    val viewModel: SplashViewModel = viewModel(
        factory = SplashViewModelFactory(repository)
    )
    val destination by viewModel.destination.collectAsState()

    val scale = remember { Animatable(0f) }
    
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = { OvershootInterpolator(2f).getInterpolation(it) }
            )
        )
    }

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.Login -> onNavigateToLogin()
            SplashDestination.Discovery -> onNavigateToDiscovery()
            SplashDestination.ProfileSetup -> onNavigateToProfileSetup()
            SplashDestination.Loading -> { /* Keep showing splash */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                tonalElevation = 12.dp,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "K",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Kaushalya",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            )
            
            Text(
                text = "Karnataka",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 4.sp
            )
        }
        
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .size(32.dp),
            color = Color.White,
            strokeWidth = 3.dp
        )
    }
}

private class OvershootInterpolator(private val tension: Float) : android.view.animation.Interpolator {
    override fun getInterpolation(input: Float): Float {
        val t = input - 1.0f
        return t * t * ((tension + 1) * t + tension) + 1.0f
    }
}
