package com.example.workmatetest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.workmatetest.domain.model.Contact
import com.example.workmatetest.domain.model.Location
import com.example.workmatetest.domain.model.Person
import com.example.workmatetest.domain.model.User
import java.io.File

@Composable
fun ProfileScreen(
    onNavigateUp: () -> Unit,
    user: User,
) {
    var selectedTab by remember { mutableStateOf(ProfileTab.USER) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                IconButton(
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back"
                    )
                }

            }
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserPhoto(user.pictureFilePath)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${user.person.firstname} ${user.person.lastname}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            ProfileTabRow(selectedTab = selectedTab) { selectedTab = it }

            Spacer(Modifier.height(24.dp))

            when (selectedTab) {
                ProfileTab.USER -> UserInfoSection(user.person)
                ProfileTab.PHONE -> ContactInfoSection(user.contact)
                ProfileTab.LOCATION -> LocationInfoSection(user.location)
            }
        }
    }
}

@Composable
fun UserPhoto(filePath: String) {
    AsyncImage(
        model = File(filePath),
        contentDescription = "User photo",
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        contentScale = ContentScale.Crop
    )
}

enum class ProfileTab { USER, PHONE, LOCATION }

@Composable
fun ProfileTabRow(selectedTab: ProfileTab, onSelect: (ProfileTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ProfileTabButton(
            icon = Icons.Default.Person,
            selected = selectedTab == ProfileTab.USER,
            onClick = { onSelect(ProfileTab.USER) }
        )

        ProfileTabButton(
            icon = Icons.Default.Phone,
            selected = selectedTab == ProfileTab.PHONE,
            onClick = { onSelect(ProfileTab.PHONE) }
        )

        ProfileTabButton(
            icon = Icons.Default.LocationOn,
            selected = selectedTab == ProfileTab.LOCATION,
            onClick = { onSelect(ProfileTab.LOCATION) }
        )
    }
}

@Composable
fun ProfileTabButton(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color =
        if(selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant

    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun UserInfoSection(person: Person) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow("Gender", person.gender)
        InfoRow("Age", person.age)
        InfoRow("Birth date", person.dateOfBirth)
        InfoRow("Nationality", person.nationality)
    }
}

@Composable
fun ContactInfoSection(contact: Contact) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow("Phone", contact.phoneNumber)
        InfoRow("Email", contact.email)
    }
}

@Composable
fun LocationInfoSection(location: Location) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow("Country", location.country)
        InfoRow("State", location.state)
        InfoRow("City", location.city)
        InfoRow("Street", location.street)
        InfoRow("Post code", location.postCode)
        InfoRow("Timezone", location.timezone)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(4.dp))
    }
}
