package com.example.workmatetest.presentation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.workmatetest.domain.model.User
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun UsersScreen(
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: UserViewModel
) {
    val state = viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }

    var previousSize: Int? by remember { mutableStateOf(null) }

    LaunchedEffect(state.value.users.size) {
        val newSize = state.value.users.size

        previousSize?.let {
            if(newSize > it && state.value.error == null) {
                val lastIndex = newSize - 1

                listState.animateScrollToItem(lastIndex)

                highlightedIndex = lastIndex
                delay(2000)
                highlightedIndex = null
            }
        }

        previousSize = newSize
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.value.error) {
        if(state.value.error != null) {

            val result = snackbarHostState.showSnackbar(
                message = "No internet connection",
                actionLabel = "Retry",
                duration = SnackbarDuration.Short
            )

            when (result) {
                SnackbarResult.ActionPerformed -> viewModel.retryGenerateNewUser()
                SnackbarResult.Dismissed -> {}
            }

            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                CustomHeader("Users (${state.value.users.size})", onNavigateUp)

                if(state.value.isLoading) {
                    LinearProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(state.value.users) { index, it ->
                UserCard(
                    user = it,
                    highlight = highlightedIndex == index,
                    onClick = { onNavigate("profile_screen/${index}") },
                    onRemove = {
                        if(state.value.users.size == 1) {
                            onNavigateUp()
                        }
                        viewModel.removeUser(user = it)
                    }
                )
            }
        }
    }
}

@Composable
fun UserCard(user: User, highlight: Boolean, onClick: () -> Unit, onRemove: (User) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val highlightColor by infiniteTransition.animateColor(
        initialValue = Color.Transparent,
        targetValue = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "highlight new user"
    )

    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(highlight) highlightColor else Color.Unspecified
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = File(user.pictureFilePath),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${user.person.firstname} ${user.person.lastname}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${user.location.city}, ${user.location.country}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = user.contact.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = user.contact.phoneNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Box(Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Actions"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Remove") },
                        onClick = {
                            menuExpanded = false
                            onRemove(user)
                        }
                    )
                }
            }
        }
    }
}



