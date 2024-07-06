package com.hello.ebookreader.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.hello.ebookreader.ui.theme.BackgroundColor
import com.hello.ebookreader.ui.theme.OnPrimaryColor
import com.hello.ebookreader.ui.theme.PrimaryColor
import com.hello.ebookreader.ui.theme.SurfaceColor
import com.hello.ebookreader.ui.theme.TextPrimaryColor
import com.hello.ebookreader.ui.theme.TextSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksByCategory(
    category: String,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController
) {
    val res = viewModel.state.value
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(category) {
        viewModel.loadBooksByCategory(category)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category,
                        color = OnPrimaryColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnPrimaryColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Implement filter functionality */ }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = OnPrimaryColor
                        )
                    }
                },
                colors = topAppBarColors(containerColor = PrimaryColor),
                modifier = Modifier.shadow(elevation = 8.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundColor)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            when {
                res.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryColor
                        )
                    }
                }
                res.error.isNotEmpty() -> ErrorMessage(res.error)
                res.categoryItems.isNotEmpty() -> {
                    val filteredBooks = res.categoryItems.filter {
                        it.bookName.contains(searchQuery, ignoreCase = true)
                    }
                    BookListCategory(
                        bookModels = filteredBooks,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(SurfaceColor),
        textStyle = TextStyle(color = TextPrimaryColor, fontSize = 16.sp),
        placeholder = {
            Text("Search books...", color = TextSecondaryColor)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = PrimaryColor
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = TextSecondaryColor
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceColor,
            unfocusedContainerColor = SurfaceColor,
            disabledContainerColor = SurfaceColor,
            cursorColor = PrimaryColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun BookListCategory(bookModels: List<BookModel>, navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(bookModels) { _, book ->
            BookItemCategory(
                bookModel = book,
                onBookClick = {
                    navController.navigate(
                        NavigationItem.ShowPdfScreen(
                            url = book.bookUrl,
                            bookName = book.bookName
                        )
                    )
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
fun BookItemCategory(bookModel: BookModel, onBookClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onBookClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = "https://m.media-amazon.com/images/I/31RW8HQ31WL._SY445_SX342_.jpg"
                    ?: Icons.Default.Book,
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = bookModel.bookName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimaryColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookModel.category,
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
                Text(
                    text = "30% Read", // Replace with actual percentage
                    fontSize = 12.sp,
                    color = TextSecondaryColor
                )
            }
        }
    }
}




