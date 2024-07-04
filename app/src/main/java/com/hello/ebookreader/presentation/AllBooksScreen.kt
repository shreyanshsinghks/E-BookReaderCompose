package com.hello.ebookreader.presentation

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
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AllBooksScreen(viewModel: ViewModel = hiltViewModel()) {
    val res = viewModel.state.value
    if(res.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
    if(res.error.isNotEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = res.error)
        }
    }
    if(res.items.isNotEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn {
                items(res.items){
                    Card (modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ){
                        Row {
                            Text(text = it.bookName)
                            Text(text = it.bookUrl)
                        }
                    }
                }
            }
        }
    }

}