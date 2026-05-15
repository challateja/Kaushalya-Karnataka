package com.example.kaushalya_karnataka.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.PhoneCallback
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kaushalya_karnataka.components.EmptyState
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.*
import com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModel
import com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditorScreen(
    onLogout: () -> Unit, // Renamed and specialized
    repository: WorkerRepository = remember { WorkerRepository() }
) {
    val viewModel: ProfileEditorViewModel = viewModel(
        factory = ProfileEditorViewModelFactory(repository)
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableIntStateOf(0) }
    
    // UI Local State
    var editingServiceId by remember { mutableStateOf<String?>(null) }
    var showAddService by remember { mutableStateOf(false) }
    var newServiceTitle by remember { mutableStateOf("") }
    var newServicePrice by remember { mutableStateOf("") }
    var newPriceType by remember { mutableStateOf(PriceType.FIXED) }

    // Multi-purpose Image Launcher
    var pickingForPortfolio by remember { mutableStateOf(true) }
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (pickingForPortfolio) {
                viewModel.addPortfolioImage(it.toString(), "Verified Work Sample")
            } else {
                viewModel.onImageChange(it.toString())
            }
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(strokeWidth = 3.dp)
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Business Hub",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                },
                actions = {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                    } else {
                        IconButton(onClick = { 
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out my professional profile on Kaushalya Karnataka: ${uiState.name}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                        }) {
                            Icon(Icons.Default.Share, "Share Profile")
                        }
                        Button(
                            onClick = { 
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.saveProfile(onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Profile saved and live for all users!")
                                    }
                                }) 
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Dashboard Summary Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.clickable { 
                            pickingForPortfolio = false
                            imageLauncher.launch("image/*")
                        }) {
                            Surface(
                                modifier = Modifier.size(72.dp),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                tonalElevation = 4.dp
                            ) {
                                AsyncImage(
                                    model = uiState.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                            if (uiState.worker?.isVerified == true) {
                                Icon(
                                    Icons.Default.CheckCircle, 
                                    null, 
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).background(Color.White, CircleShape)
                                )
                            }
                        }
                        Spacer(Modifier.width(20.dp))
                        Column {
                            Text(uiState.name.ifBlank { "Setup Identity" }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                            Text(uiState.selectedCategory.displayName, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("Total Leads", uiState.hireRequests.size.toString(), Icons.Default.ConnectWithoutContact)
                        StatItem("Rating", String.format(Locale.getDefault(), "%.1f", uiState.worker?.rating ?: 0.0), Icons.Default.Star)
                        StatItem("Status", if(uiState.worker?.isVerified == true) "Verified" else "Basic", Icons.Default.GppGood)
                    }
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                divider = {},
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                val tabs = listOf("Identity", "Works", "Portfolio", "Inquiries")
                tabs.forEachIndexed { index, title ->
                    val hasBadge = index == 3 && uiState.unreadLeadsCount > 0
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            BadgedBox(badge = { if(hasBadge) Badge { Text(uiState.unreadLeadsCount.toString()) } }) {
                                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))) {
                when (selectedTab) {
                    0 -> BasicInfoTab(uiState, viewModel, onLogout)
                    1 -> ServicesTab(
                        uiState, viewModel, showAddService, { showAddService = it }, 
                        newServiceTitle, { newServiceTitle = it }, 
                        newServicePrice, { newServicePrice = it },
                        newPriceType, { newPriceType = it },
                        editingServiceId, { editingServiceId = it }
                    )
                    2 -> PortfolioTab(uiState, viewModel) { 
                        pickingForPortfolio = true
                        imageLauncher.launch("image/*") 
                    }
                    3 -> LeadsTab(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoTab(
    uiState: com.example.kaushalya_karnataka.viewmodel.ProfileEditorUiState, 
    viewModel: com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModel,
    onLogout: () -> Unit
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var skillInput by remember { mutableStateOf("") }
    val haptics = LocalHapticFeedback.current

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(24.dp)) {
        if (uiState.worker?.isVerified != true) {
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), shape = RoundedCornerShape(16.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Upgrade to Verified", fontWeight = FontWeight.Bold)
                            Text("Build 3x more trust with neighbors.", style = MaterialTheme.typography.bodySmall)
                        }
                        TextButton(onClick = { }) { Text("Learn How") }
                    }
                }
            }
        }
        item {
            Text("Core Identity", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = uiState.name, onValueChange = { viewModel.onNameChange(it) }, label = { Text("Display / Business Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = it }) {
                OutlinedTextField(value = uiState.selectedCategory.displayName, onValueChange = {}, readOnly = true, label = { Text("Primary Specialty") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                    Category.entries.forEach { category -> DropdownMenuItem(text = { Text(category.displayName) }, onClick = { viewModel.onCategoryChange(category); categoryExpanded = false }) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.location, onValueChange = { viewModel.onLocationChange(it) }, label = { Text("Area of Operation") }, leadingIcon = { Icon(Icons.Default.NearMe, null, modifier = Modifier.size(18.dp)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Skills & Expertise", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("Add tags for things you can do (e.g. Wall Painting)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            
            OutlinedTextField(
                value = skillInput,
                onValueChange = { skillInput = it },
                label = { Text("Add Skill") },
                placeholder = { Text("Type and press +") },
                trailingIcon = {
                    IconButton(onClick = { 
                        if (skillInput.isNotBlank()) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.addSkill(skillInput.trim())
                            skillInput = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
            )
            
            Spacer(Modifier.height(12.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(uiState.skills) { skill ->
                    InputChip(
                        selected = false,
                        onClick = { viewModel.removeSkill(skill) },
                        label = { Text(skill) },
                        trailingIcon = { Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp)) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Business Bio", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = uiState.bio, onValueChange = { viewModel.onBioChange(it) }, label = { Text("Bio / Experience") }, placeholder = { Text("Tell neighbors about your experience and quality standards...") }, modifier = Modifier.fillMaxWidth().height(140.dp), shape = RoundedCornerShape(16.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
        }

        item {
            Spacer(Modifier.height(48.dp))
            TextButton(
                onClick = { showLogoutConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Logout from Professional Account")
            }
        }
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Logout?") },
            text = { Text("Are you sure you want to logout? You will need to sign in again to access your dashboard.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    viewModel.logout(onLogout)
                }) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ServicesTab(
    uiState: com.example.kaushalya_karnataka.viewmodel.ProfileEditorUiState,
    viewModel: com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModel,
    showAdd: Boolean,
    onShowAdd: (Boolean) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    priceType: PriceType,
    onPriceTypeChange: (PriceType) -> Unit,
    editingId: String?,
    onEditingIdChange: (String?) -> Unit
) {
    val haptics = LocalHapticFeedback.current
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(24.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Works I Offer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (!showAdd && editingId == null) {
                    FilledTonalButton(onClick = { onShowAdd(true) }, shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.AddCircleOutline, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Add Work")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (showAdd || editingId != null) {
            item {
                val isEditing = editingId != null
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(if (isEditing) "Modify Work Item" else "Add New Work Item", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Work/Service Title") }, placeholder = { Text("e.g. Pipe Repair, Fan Installation") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(value = price, onValueChange = onPriceChange, label = { Text("Price (₹) - Optional") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Pricing Type", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = priceType == PriceType.FIXED, onClick = { onPriceTypeChange(PriceType.FIXED) })
                                    Text("Fixed", style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.width(8.dp))
                                    RadioButton(selected = priceType == PriceType.STARTING_AT, onClick = { onPriceTypeChange(PriceType.STARTING_AT) })
                                    Text("Start", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { onShowAdd(false); onEditingIdChange(null); onTitleChange(""); onPriceChange("") }) { Text("Cancel") }
                            Button(onClick = {
                                val p = price.toIntOrNull() ?: 0
                                if (title.isNotBlank()) {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.addOrUpdateService(editingId, title, p, priceType)
                                    onTitleChange(""); onPriceChange(""); onShowAdd(false); onEditingIdChange(null)
                                }
                            }) { Text(if (isEditing) "Save Changes" else "Add to Profile") }
                        }
                    }
                }
            }
        }

        if (uiState.services.isEmpty() && !showAdd && editingId == null) {
            item {
                EmptyState(
                    title = "No Works Listed",
                    description = "Add specific things you can do like 'Pipe Leak Repair' or 'Furniture Assembly' to help neighbors hire you.",
                    icon = Icons.Default.Handyman,
                    action = {
                        Button(onClick = { onShowAdd(true) }) { Text("Add Your First Work") }
                    }
                )
            }
        } else {
            items(uiState.services) { service ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).clickable {
                    onEditingIdChange(service.id); onTitleChange(service.title ?: ""); onPriceChange(service.price?.toString() ?: ""); onPriceTypeChange(service.getPriceTypeEnum())
                }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(service.title ?: "Untitled", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                            if ((service.price ?: 0) > 0) {
                                Text("${service.getPriceTypeEnum().label} ₹${service.price}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                            } else {
                                Text("Price on Request", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                        Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(16.dp))
                        IconButton(onClick = { viewModel.removeService(service.id) }) {
                            Icon(Icons.Default.DeleteOutline, "Remove", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PortfolioTab(uiState: com.example.kaushalya_karnataka.viewmodel.ProfileEditorUiState, viewModel: com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModel, onPickImage: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Verified Work History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Customers are 40% more likely to hire workers with work photos.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = onPickImage, shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.PhotoCamera, null)
                Spacer(Modifier.width(8.dp))
                Text("Add Photo")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (uiState.portfolioImages.isEmpty()) {
            EmptyState(
                title = "Empty Gallery",
                description = "Showcase your best projects to build trust with potential customers.",
                icon = Icons.Default.AddPhotoAlternate,
                action = {
                    Button(onClick = onPickImage) { Text("Upload Work Photos") }
                }
            )
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.portfolioImages) { image ->
                    Card(modifier = Modifier.aspectRatio(1f), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                        Box {
                            AsyncImage(model = image.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            IconButton(onClick = { viewModel.removePortfolioImage(image.id) }, modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).size(28.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)) { Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadsTab(uiState: com.example.kaushalya_karnataka.viewmodel.ProfileEditorUiState, viewModel: com.example.kaushalya_karnataka.viewmodel.ProfileEditorViewModel) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(24.dp)) {
        item {
            Text("Customer Inquiries", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Respond quickly to secure the booking.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (uiState.hireRequests.isEmpty()) {
            item {
                EmptyState(
                    title = "No Leads Yet",
                    description = "Your services haven't received inquiries yet. Keep your profile updated and share it with your neighbors!",
                    icon = Icons.Default.ConnectWithoutContact
                )
            }
        } else {
            items(uiState.hireRequests) { request ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { 
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.deleteLead(request.id)
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val alignment = Alignment.CenterEnd
                        val color = MaterialTheme.colorScheme.errorContainer
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = 16.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(color)
                                .padding(horizontal = 24.dp),
                            contentAlignment = alignment
                        ) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    },
                    enableDismissFromStartToEnd = false,
                    content = {
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = if (request.isContacted == true) Color.White else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)), elevation = CardDefaults.cardElevation(if (request.isContacted == true) 0.dp else 4.dp)) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(color = MaterialTheme.colorScheme.primary, shape = CircleShape, modifier = Modifier.size(40.dp)) { Box(contentAlignment = Alignment.Center) { Text(request.customerName?.firstOrNull()?.toString()?.uppercase() ?: "C", color = Color.White, fontWeight = FontWeight.Black) } }
                                        Spacer(Modifier.width(12.dp))
                                        Column {
                                            Text(request.customerName ?: "Potential Customer", fontWeight = FontWeight.Black)
                                            Text(request.phone ?: "No Phone Provided", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    if (request.isContacted != true) { Badge(containerColor = MaterialTheme.colorScheme.primary) { Text("ACTION NEEDED", modifier = Modifier.padding(4.dp)) } }
                                }
                                Spacer(Modifier.height(16.dp))
                                Surface(color = Color.Black.copy(alpha = 0.03f), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) { Text(request.message ?: "Interested in hiring you.", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium) }
                                Spacer(Modifier.height(20.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(onClick = { 
                                        if (!request.phone.isNullOrBlank()) {
                                            val intent = Intent(Intent.ACTION_DIAL, request.phone!!.toUri())
                                            context.startActivity(intent)
                                        }
                                    }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))) { Icon(Icons.Default.Phone, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Call Now") }
                                    OutlinedButton(onClick = { 
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.toggleRequestContacted(request.id) 
                                    }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Icon(if (request.isContacted == true) Icons.Default.DoneAll else Icons.AutoMirrored.Filled.PhoneCallback, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text(if (request.isContacted == true) "Contacted" else "Mark Done") }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
