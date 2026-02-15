package com.example.mytestapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownShowcaseScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Dropdown Showcase",
            style = MaterialTheme.typography.headlineSmall
        )

        SimpleDropdown()

        ExposedDropdown()

        OverflowMenuDropdown()

        MultiSelectDropdown()

        DisabledDropdown()

    }
}


@Composable
fun SimpleDropdown() {
    val options = listOf("Centimeters", "Meters", "Millimeters", "Feet")
    var selected by remember { mutableStateOf("Select Unit") }
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selected)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdown() {
    val options = listOf("Low", "Medium", "High")
    var selected by remember { mutableStateOf(options[0]) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Priority") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selected = option
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun OverflowMenuDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("None") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        selectedAction = "Edit"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        selectedAction = "Delete"
                        expanded = false
                    }
                )
            }
        }

        Text(text = "Action: $selectedAction")
    }
}



@Composable
fun MultiSelectDropdown() {
    val options = listOf("Red", "Green", "Blue")
    val selectedItems = remember { mutableStateListOf<String>() }
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box {
            Button(onClick = { expanded = true }) {
                Text("Select Colors")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { color ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = color in selectedItems,
                                    onCheckedChange = {
                                        if (it) selectedItems.add(color)
                                        else selectedItems.remove(color)
                                    }
                                )
                                Text(color)
                            }
                        },
                        onClick = {}
                    )
                }
            }
        }

        Text("Selected: ${selectedItems.joinToString()}")
    }
}




@Composable
fun DisabledDropdown() {
    var isEnabled by remember { mutableStateOf(true) }

    Button(
        enabled = isEnabled,
        onClick = {
            isEnabled = !isEnabled
        }
    ) {
        Text(if (isEnabled) "Enabled" else "Disabled")
        Icon(
            Icons.Default.ArrowDropDown,
            contentDescription = null
        )
    }
}




