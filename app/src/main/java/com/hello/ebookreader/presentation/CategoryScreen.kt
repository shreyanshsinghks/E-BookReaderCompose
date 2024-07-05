package com.hello.ebookreader.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hello.ebookreader.presentation.navigation.NavigationItem
import com.hello.ebookreader.presentation.viewmodel.ViewModel

@Composable
fun CategoryScreen(viewModel: ViewModel = hiltViewModel(), navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val bookCategory = viewModel.state.value.category
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(bookCategory){
                Text(text = it.name, modifier = Modifier.clickable {
                    navController.navigate(NavigationItem.BooksByCategory(it.name))
                })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}