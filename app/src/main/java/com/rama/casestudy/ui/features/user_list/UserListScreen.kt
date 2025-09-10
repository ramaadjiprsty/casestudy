package com.rama.casestudy.ui.features.user_list

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.navigation.Screen
import com.rama.casestudy.util.Resource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserListViewModel = koinViewModel()
) {
    val state = viewModel.userListState.collectAsState().value
    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Success -> {
                    state.data?.let { users ->
                        val onItemClick = { user: User ->
                            navController.navigate(Screen.UserDetail.createRoute(user.id))
                        }

                        // Cek orientasi layar untuk menentukan layout
                        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            UserGridContent(users = users, onItemClick = onItemClick)
                        } else {
                            UserListContent(users = users, onItemClick = onItemClick)
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = state.message ?: "An unknown error occurred",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun UserListContent(users: List<User>, onItemClick: (User) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserItem(user = user, onClick = { onItemClick(user) })
        }
    }
}

@Composable
private fun UserGridContent(users: List<User>, onItemClick: (User) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 220.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(users) { user ->
            UserItem(user = user, onClick = { onItemClick(user) })
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    val cardColor = when {
        user.age <= 30 -> Color(0xFFC8E6C9) // Warna Hijau Muda
        user.age in 31..40 -> Color(0xFFFFF9C4) // Warna Kuning Muda
        else -> Color(0xFFFFE0B2) // Warna Oranye Muda
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (user.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = user.fullName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "No Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.fullName, style = MaterialTheme.typography.titleMedium)
                Text(text = "${user.age} years old", style = MaterialTheme.typography.titleSmall)
                Text(text = user.jobTitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}