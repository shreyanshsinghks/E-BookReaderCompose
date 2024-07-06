package com.hello.ebookreader.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
fun AllBooksScreen(viewModel: ViewModel = hiltViewModel(), navController: NavController) {
    val res = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            res.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            res.error.isNotEmpty() -> {
                Text(
                    text = res.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            res.items.isNotEmpty() -> {
                LazyColumn {
                    items(res.items) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(NavigationItem.ShowPdfScreen(it.bookUrl))
                                },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column {
                                Row {
                                    Text(text = it.bookName)
                                    Text(text = it.bookUrl)
                                }
                                Text(text = it.category)
                            }
                        }
                    }
                }
            }

            else -> {
                Text(
                    text = "No books available",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}