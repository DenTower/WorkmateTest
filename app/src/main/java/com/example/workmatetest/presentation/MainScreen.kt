package com.example.workmatetest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.workmatetest.GENDERS
import com.example.workmatetest.NATIONALITIES

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit,
    viewModel: UserViewModel
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Generate User",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var selectedGender: String? by remember { mutableStateOf(null) }
            var selectedNationality: String? by remember { mutableStateOf(null) }

            Column {
                ParameterSelection(
                    title = "Select gender",
                    selectableItems = GENDERS,
                    selected = selectedGender,
                    onSelected = { selectedGender = it }
                )
                ParameterSelection(
                    title = "Select nationality",
                    selectableItems = NATIONALITIES,
                    selected = selectedNationality,
                    onSelected = { selectedNationality = it }
                )
            }

            Column {
                Button(
                    onClick = {
                        onNavigate("users_screen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Show Users")
                }

                Button(
                    onClick = {
                        viewModel.generateNewUser(
                            gender = selectedGender,
                            nationality = selectedNationality
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Generate")
                }
            }
        }
    }
}

@Composable
fun ParameterSelection(
    title: String,
    selectableItems: List<String>,
    selected: String?,
    onSelected: (String) -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }

    if(dialogOpen) {
        AlertDialog(onDismissRequest = { dialogOpen = false }, text = {
            LazyColumn(modifier = Modifier.padding()) {
                items(selectableItems) { item ->
                    Text(
                        item, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelected(item)
                                dialogOpen = false
                            }
                            .padding(12.dp))
                }
            }
        }, confirmButton = {})
    }


    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(title)

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .clickable { dialogOpen = true }
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(1.dp)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(selected?: "Not selected")

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "dropdown"
            )
        }
    }

}