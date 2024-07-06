package com.hello.ebookreader.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hello.ebookreader.presentation.viewmodel.ViewModel
import com.hello.ebookreader.ui.theme.ErrorColor
import com.hello.ebookreader.ui.theme.OnPrimaryColor
import com.hello.ebookreader.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(viewModel: ViewModel = hiltViewModel(), navController: NavController) {
    val state = viewModel.state.value
    val bookName = remember { mutableStateOf("") }
    val bookUrl = remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    val uniqueCategories = viewModel.state.value.category.map { it.name }.distinct().sorted()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Book", color = OnPrimaryColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OnPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = bookName.value,
                onValueChange = { bookName.value = it },
                label = { Text("Book Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bookUrl.value,
                onValueChange = { bookUrl.value = it },
                label = { Text("Book URL") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},  // Read-only
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .onFocusChanged { if (it.isFocused) isExpanded = true }
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    uniqueCategories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                category = item
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.addBook(
                        bookUrl = bookUrl.value,
                        bookName = bookName.value,
                        category = category
                    )
                    navController.navigateUp()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Book")
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = PrimaryColor
                )
            }

            if (state.error.isNotEmpty()) {
                Text(
                    text = state.error,
                    color = ErrorColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
