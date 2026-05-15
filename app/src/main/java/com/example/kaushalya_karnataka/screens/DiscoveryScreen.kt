package com.example.kaushalya_karnataka.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kaushalya_karnataka.components.CategoryChip
import com.example.kaushalya_karnataka.components.EmptyState
import com.example.kaushalya_karnataka.components.ShimmerWorkerCard
import com.example.kaushalya_karnataka.components.WorkerCard
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.Category
import com.example.kaushalya_karnataka.models.Worker
import com.example.kaushalya_karnataka.viewmodel.DiscoveryViewModel
import com.example.kaushalya_karnataka.viewmodel.DiscoveryViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    onWorkerClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    repository: WorkerRepository = remember { WorkerRepository() }
) {
    val viewModel: DiscoveryViewModel = viewModel(
        factory = DiscoveryViewModelFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsState()
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val haptics = LocalHapticFeedback.current
    
    var showLocationMenu by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 2.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { showLocationMenu = true }
                            ) {
                                Icon(
                                    Icons.Default.LocationOn, 
                                    contentDescription = null, 
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = uiState.selectedLocation,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                                
                                DropdownMenu(
                                    expanded = showLocationMenu,
                                    onDismissRequest = { showLocationMenu = false },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                ) {
                                    uiState.availableLocations.forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location) },
                                            onClick = {
                                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                viewModel.onLocationSelected(location)
                                                showLocationMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Hello, ${currentUser?.displayName?.split(" ")?.firstOrNull() ?: "Citizen"}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp
                            )
                        }
                        
                        Row {
                            IconButton(
                                onClick = { /* Notifications */ },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.NotificationsNone, "Notifications")
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = onProfileClick,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Icon(Icons.Default.AccountCircle, "Profile", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { 
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.refresh() 
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Connection Error Alert
                if (uiState.error != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CloudOff, null, tint = MaterialTheme.colorScheme.error)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "Connection Error: ${uiState.error}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                // Search & Filter Bar
                item {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search services...") },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                        FilledIconButton(
                            onClick = { 
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                showFilterSheet = true 
                            },
                            modifier = Modifier.size(56.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Icon(Icons.Default.Tune, "Filters")
                        }
                    }
                }

                // NEW: Recently Active Professionals (Showing updates to all users)
                if (!uiState.isLoading && uiState.workers.any { (it.updatedAt ?: 0L) > 0 }) {
                    item {
                        Spacer(Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Recently Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                "New Offers",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val recentlyActive = uiState.workers.sortedByDescending { it.updatedAt ?: 0L }.take(5)
                            items(recentlyActive) { pro ->
                                FeaturedProCard(pro, onClick = { onWorkerClick(pro.id) }, showActiveBadge = true)
                            }
                        }
                    }
                }

                // Specialties Grid
                item {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        "Popular Skills",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    val specialties = Category.entries.filter { it != Category.OTHER }.take(4)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        specialties.forEach { category ->
                            SpecialtyGridItem(
                                category = category,
                                isSelected = uiState.selectedCategory == category,
                                onClick = { 
                                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    viewModel.onCategorySelected(category) 
                                }
                            )
                        }
                    }
                }

                // Feed Header
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Verified Professionals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(Modifier.width(8.dp))
                        Surface(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = CircleShape) {
                            Text(
                                text = uiState.filteredWorkers.size.toString(),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (uiState.isLoading) {
                    items(5) {
                        ShimmerWorkerCard(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp))
                    }
                } else {
                    items(uiState.filteredWorkers, key = { it.id }) { worker ->
                        WorkerCard(
                            worker = worker,
                            onClick = { onWorkerClick(worker.id) },
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                        )
                    }
                    
                    if (uiState.filteredWorkers.isEmpty()) {
                        item {
                            EmptyState(
                                title = "No Matches Found",
                                description = "We couldn't find any professionals matching your current filters in ${uiState.selectedLocation}.",
                                icon = Icons.Default.PersonSearch,
                                action = {
                                    Button(onClick = { 
                                        viewModel.onCategorySelected(null)
                                        viewModel.onLocationSelected("All Karnataka")
                                        viewModel.onMinRatingChanged(0.0)
                                        viewModel.onMaxPriceChanged(10000)
                                        viewModel.onSearchQueryChanged("")
                                    }, shape = RoundedCornerShape(12.dp)) {
                                        Text("Clear All Filters")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            FilterContent(
                selectedLocation = uiState.selectedLocation,
                locations = uiState.availableLocations,
                minRating = uiState.minRating,
                maxPrice = uiState.maxPrice,
                onLocationSelect = { viewModel.onLocationSelected(it) },
                onMinRatingChange = { viewModel.onMinRatingChanged(it) },
                onMaxPriceChange = { viewModel.onMaxPriceChanged(it) },
                onClear = {
                    viewModel.onCategorySelected(null)
                    viewModel.onLocationSelected("All Karnataka")
                    viewModel.onMinRatingChanged(0.0)
                    viewModel.onMaxPriceChanged(10000)
                    showFilterSheet = false
                }
            )
        }
    }
}

@Composable
fun FilterContent(
    selectedLocation: String,
    locations: List<String>,
    minRating: Double,
    maxPrice: Int,
    onLocationSelect: (String) -> Unit,
    onMinRatingChange: (Double) -> Unit,
    onMaxPriceChange: (Int) -> Unit,
    onClear: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 48.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Refine Discovery", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            TextButton(onClick = onClear) {
                Text("Reset", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text("Operating District", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(locations) { loc ->
                CategoryChip(
                    label = loc,
                    isSelected = selectedLocation == loc,
                    onClick = { onLocationSelect(loc) }
                )
            }
        }
        
        Spacer(Modifier.height(32.dp))
        
        Text("Minimum Rating", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = minRating.toFloat(),
                onValueChange = { onMinRatingChange(it.toDouble()) },
                valueRange = 0f..5f,
                steps = 4,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${minRating.roundToInt()}+ ★",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        
        Text("Budget (Up to)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = maxPrice.toFloat(),
                onValueChange = { onMaxPriceChange(it.toInt()) },
                valueRange = 100f..10000f,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(16.dp))
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (maxPrice >= 10000) "Any" else "₹$maxPrice",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = { /* Sheet dismisses automatically on click outside or via system back */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Apply Filters", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RowScope.SpecialtyGridItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f).clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            tonalElevation = if (isSelected) 4.dp else 0.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                val icon = when(category) {
                    Category.ELECTRICIAN -> Icons.Default.Bolt
                    Category.PLUMBER -> Icons.Default.WaterDrop
                    Category.CARPENTER -> Icons.Default.Handyman
                    Category.PAINTER -> Icons.Default.FormatPaint
                    else -> Icons.Default.Category
                }
                Icon(icon, null, tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(text = category.displayName.split(" ").first(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun FeaturedProCard(worker: Worker, onClick: () -> Unit, showActiveBadge: Boolean = false) {
    Card(
        modifier = Modifier.width(160.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                AsyncImage(model = worker.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                
                // Show "JUST UPDATED" badge if active within last 24h
                val isRecentlyUpdated = (System.currentTimeMillis() - (worker.updatedAt ?: 0L)) < 86400000
                if (showActiveBadge && isRecentlyUpdated) {
                    Surface(
                        modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF22C55E)
                    ) {
                        Text(
                            text = "JUST UPDATED",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 8.sp
                        )
                    }
                }

                Surface(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFEAB308), modifier = Modifier.size(10.dp))
                        Text(text = String.format(Locale.getDefault(), "%.1f", worker.rating ?: 0.0), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = worker.name ?: "Pro", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = worker.getCategoryEnum().displayName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
