package com.example.workmatetest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workmatetest.COUNTRIES
import com.example.workmatetest.GENDERS

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit,
    viewModel: UserViewModel
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            CustomHeader("Generate User")
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val generationParams = state.value.generationParams
            var selectedGender: String? by remember { mutableStateOf(generationParams.gender) }
            var selectedNationality: String? by remember { mutableStateOf(generationParams.nationality) }

            Column {
                ParameterSelectorCard(
                    title = "Select gender",
                    items = GENDERS,
                    selected = selectedGender,
                    onSelect = { selectedGender = it }
                )
                ParameterSelectorCard(
                    title = "Select nationality",
                    items = COUNTRIES.map { it.name },
                    selected = selectedNationality,
                    onSelect = { selectedNationality = it }
                )
                ResetButton(modifier = Modifier.align(Alignment.End)) {
                    selectedGender = null
                    selectedNationality = null
                }
            }
            val usersSize = state.value.users.size

            Column {
                Button(
                    onClick = { onNavigate("users_screen") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = usersSize > 0
                ) {
                    Text(
                        text = "Show Users ($usersSize)",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Button(
                    onClick = {
                        viewModel.generateNewUser(
                            gender = selectedGender,
                            nationality = selectedNationality
                        )
                        onNavigate("users_screen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Generate",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ParameterSelectorCard(
    title: String,
    selected: String?,
    items: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { expanded = true }
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selected ?: "Not selected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(if(expanded) 180f else 0f)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ResetButton(modifier: Modifier = Modifier, onReset: () -> Unit) {
    TextButton(onClick = onReset, modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset",
        )
        Spacer(Modifier.width(4.dp))
        Text("Reset")
    }
}

