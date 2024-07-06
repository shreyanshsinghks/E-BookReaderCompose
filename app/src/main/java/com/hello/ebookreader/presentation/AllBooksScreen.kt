package com.hello.ebookreader.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hello.ebookreader.common.BookModel
import com.hello.ebookreader.presentation.navigation.NavigationItem
import com.hello.ebookreader.presentation.viewmodel.ViewModel
import com.hello.ebookreader.ui.theme.AccentColor1
import com.hello.ebookreader.ui.theme.ErrorColor
import com.hello.ebookreader.ui.theme.PrimaryColor
import com.hello.ebookreader.ui.theme.TextPrimaryColor
import com.hello.ebookreader.ui.theme.TextSecondaryColor

@Composable
fun AllBooksScreen(viewModel: ViewModel = hiltViewModel(), navController: NavController) {
    val res = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            res.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryColor
                )
            }
            res.error.isNotEmpty() -> {
                ErrorMessage(error = res.error)
            }
            res.items.isNotEmpty() -> {
                BookList(books = res.items, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookList(books: List<BookModel>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(books) { index, book ->
            BookItem(
                book = book,
                onBookClick = {
                    navController.navigate(NavigationItem.ShowPdfScreen(url = book.bookUrl, bookName = book.bookName))
                },
                modifier = Modifier.animateItem(
                    fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            )
        }
    }
}

@Composable
fun BookItem(book: BookModel, onBookClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onBookClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = "https://m.media-amazon.com/images/I/31RW8HQ31WL._SY445_SX342_.jpg" ?: Icons.Default.Book,
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = book.bookName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimaryColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.category,
                    fontSize = 14.sp,
                    color = TextSecondaryColor
                )
                LinearProgressIndicator(
                    progress = {
                        0.3f // Replace with actual reading progress
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = AccentColor1,
                    trackColor = Color.LightGray,
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            color = ErrorColor,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}


