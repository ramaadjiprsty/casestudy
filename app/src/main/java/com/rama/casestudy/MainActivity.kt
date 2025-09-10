package com.rama.casestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.ui.features.user_list.UserListViewModel
import com.rama.casestudy.ui.theme.TestCaseTheme
import com.rama.casestudy.util.Resource
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestCaseTheme {
                KoinAndroidContext {
                    // Kita gunakan Scaffold sebagai layout utama
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
                        // innerPadding diterapkan agar konten tidak tertutup TopAppBar
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding), // Terapkan padding di sini
                            color = MaterialTheme.colorScheme.background
                        ) {
                            UserListScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListScreen(viewModel: UserListViewModel = koinViewModel()) {
    val state = viewModel.userListState.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                state.data?.let { users ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(users) { user ->
                            UserItem(user = user)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
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

@Composable
fun UserItem(user: User) {
    // Tentukan warna berdasarkan umur menggunakan 'when'
    val cardColor = when {
        user.age <= 30 -> Color(0xFFC8E6C9) // Warna Hijau Muda
        user.age in 31..40 -> Color(0xFFFFF9C4) // Warna Kuning Muda
        else -> Color(0xFFFFE0B2) // Warna Oranye Muda
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
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