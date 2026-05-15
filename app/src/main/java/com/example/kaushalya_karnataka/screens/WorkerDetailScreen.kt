package com.example.kaushalya_karnataka.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kaushalya_karnataka.components.*
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.viewmodel.WorkerDetailViewModel
import com.example.kaushalya_karnataka.viewmodel.WorkerDetailViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDetailScreen(
    workerId: String,
    onBackClick: () -> Unit,
    repository: WorkerRepository = remember { WorkerRepository() }
) {
    val viewModel: WorkerDetailViewModel = viewModel(
        factory = WorkerDetailViewModelFactory(repository, workerId)
    )
    val uiState by viewModel.uiState.collectAsState()
    val haptics = LocalHapticFeedback.current

    var selectedTab by remember { mutableIntStateOf(0) }
    var showHireDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(strokeWidth = 3.dp, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(16.dp))
                Text("Loading Profile...", style = MaterialTheme.typography.bodySmall)
            }
        }
        return
    }

    val currentWorker = uiState.worker
    if (currentWorker == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(uiState.error ?: "Profile not found")
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = { 
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        showHireDialog = true 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !uiState.isHiring
                ) {
                    if (uiState.isHiring) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Icon(Icons.Default.Handshake, null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Hire for Work",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = padding.calculateBottomPadding() + 32.dp)
        ) {
            // High-End Interactive Portfolio Header
            item {
                val images = currentWorker.portfolioImages ?: emptyList()
                val displayImages = if (images.isEmpty()) listOf(currentWorker.imageUrl) else images.map { it.imageUrl }
                val pagerState = rememberPagerState(pageCount = { displayImages.size })

                Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = displayImages[page],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, MaterialTheme.colorScheme.background), startY = 0f)))

                    if (displayImages.size > 1) {
                        Row(Modifier.wrapContentHeight().fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 60.dp), horizontalArrangement = Arrangement.Center) {
                            repeat(displayImages.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                                Box(modifier = Modifier.padding(4.dp).clip(CircleShape).background(color).size(8.dp))
                            }
                        }
                    }

                    IconButton(onClick = onBackClick, modifier = Modifier.padding(16.dp).statusBarsPadding().clip(CircleShape).background(Color.Black.copy(alpha = 0.3f))) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }

                    if (currentWorker.isVerified == true) {
                        Surface(modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 12.dp), shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primary, tonalElevation = 8.dp) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("VERIFIED PRO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Info Section
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).offset(y = (-40).dp)) {
                    Surface(shape = CircleShape, border = androidx.compose.foundation.BorderStroke(4.dp, MaterialTheme.colorScheme.background), modifier = Modifier.size(90.dp), shadowElevation = 8.dp) {
                        AsyncImage(model = currentWorker.imageUrl, contentDescription = null, modifier = Modifier.clip(CircleShape), contentScale = ContentScale.Crop)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = currentWorker.name ?: "Professional", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
                    Text(text = currentWorker.getCategoryEnum().displayName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, modifier = Modifier.size(16.dp), tint = Color(0xFFEAB308))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = String.format(Locale.getDefault(), "%.1f", currentWorker.rating ?: 0.0), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black)
                                Text(text = " (${currentWorker.reviewCount ?: 0})", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = currentWorker.location ?: "Karnataka", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    
                    // Expertise / Skills Section - FIXED: Replaced FlowRow with LazyRow for stability
                    if (!currentWorker.skills.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Expertise", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(currentWorker.skills ?: emptyList()) { skill ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = skill,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "About Business", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = currentWorker.bio ?: "A professional providing verified local services.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
                }
            }

            // Tabs
            item {
                TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.primary, indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedTab]), color = MaterialTheme.colorScheme.primary)
                    }
                }, modifier = Modifier.padding(horizontal = 24.dp), divider = {}) {
                    val tabs = listOf("Works", "Reviews", "Gallery")
                    tabs.forEachIndexed { index, title -> Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium) }) }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Content
            when (selectedTab) {
                0 -> {
                    val services = currentWorker.services ?: emptyList()
                    if (services.isEmpty()) item { EmptyState(title = "No Works Listed", description = "This worker hasn't listed specific items yet.", icon = Icons.Default.Handyman) }
                    else items(services) { service -> ServiceCard(service = service, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) }
                }
                1 -> {
                    item {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Neighbor Feedback", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                            TextButton(onClick = { 
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                showReviewDialog = true 
                            }) {
                                Icon(Icons.Default.RateReview, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Write a Review")
                            }
                        }
                    }
                    val reviews = currentWorker.reviews ?: emptyList()
                    if (reviews.isEmpty()) item { EmptyState(title = "Be the First", description = "No customer feedback yet. Share your experience!", icon = Icons.Default.Forum) }
                    else items(reviews) { review -> ReviewItem(review = review, modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) }
                }
                2 -> {
                    val images = currentWorker.portfolioImages ?: emptyList()
                    if (images.isEmpty()) item { EmptyState(title = "No Work Photos", description = "This professional hasn't uploaded portfolio samples.", icon = Icons.Default.PhotoLibrary) }
                    else {
                        val rows = images.chunked(2)
                        items(rows) { rowImages ->
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                rowImages.forEach { img ->
                                    Card(modifier = Modifier.weight(1f).aspectRatio(1f), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                                        AsyncImage(model = img.imageUrl, contentDescription = img.description, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                    }
                                }
                                if (rowImages.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showHireDialog) {
        HireDialog(workerName = currentWorker.name ?: "Worker", onDismiss = { showHireDialog = false }, onSubmit = { message ->
            showHireDialog = false
            viewModel.hireWorker(message) {
                snackbarMessage = "Call-back request sent to ${currentWorker.name}!"
                showSnackbar = true
            }
        })
    }

    if (showReviewDialog) {
        ReviewDialog(onDismiss = { showReviewDialog = false }, onSubmit = { rating, text ->
            showReviewDialog = false
            viewModel.addReview(rating, text) {
                snackbarMessage = "Your review has been posted!"
                showSnackbar = true
            }
        })
    }
}
