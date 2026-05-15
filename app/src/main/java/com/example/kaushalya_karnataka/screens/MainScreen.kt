package com.example.kaushalya_karnataka.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.viewmodel.MainViewModel
import com.example.kaushalya_karnataka.viewmodel.MainViewModelFactory

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Discover : BottomNavItem("discover", Icons.Default.Explore, "Discover")
    object MyBusiness : BottomNavItem("my_business", Icons.Default.BusinessCenter, "My Business")
}

@Composable
fun MainScreen(
    initialTab: Int = 0,
    onWorkerClick: (String) -> Unit,
    onLogout: () -> Unit,
    repository: WorkerRepository
) {
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(repository)
    )
    val unreadLeadsCount by mainViewModel.unreadLeadsCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var selectedItem by remember { mutableIntStateOf(initialTab) }
    var lastKnownLeadsCount by remember { mutableIntStateOf(unreadLeadsCount) }
    val items = listOf(BottomNavItem.Discover, BottomNavItem.MyBusiness)

    // Simulated Professional Notification System
    LaunchedEffect(unreadLeadsCount) {
        if (unreadLeadsCount > lastKnownLeadsCount && selectedItem == 0) {
            snackbarHostState.showSnackbar(
                message = "New Business Lead! Check your 'My Business' tab.",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
        }
        lastKnownLeadsCount = unreadLeadsCount
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item is BottomNavItem.MyBusiness && unreadLeadsCount > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text(unreadLeadsCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        },
                        label = { Text(item.label) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(
                targetState = selectedItem,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(animationSpec = tween(300)) { it } + fadeIn(animationSpec = tween(300)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut(animationSpec = tween(300)))
                    } else {
                        (slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn(animationSpec = tween(300)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { it } + fadeOut(animationSpec = tween(300)))
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "MainTabTransition"
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> DiscoveryScreen(
                        onWorkerClick = onWorkerClick,
                        onProfileClick = { selectedItem = 1 },
                        repository = repository
                    )
                    1 -> ProfileEditorScreen(
                        onLogout = onLogout, // Fixed parameter name to prevent automatic logout on save
                        repository = repository
                    )
                }
            }
        }
    }
}
