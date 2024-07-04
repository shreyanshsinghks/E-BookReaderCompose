package com.hello.ebookreader.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun TabLayout() {
    var pagerState = rememberPagerState(pageCount = { 2 })
    Column {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState) {
    VerticalPager(state = pagerState) {
        when (it) {
            0 -> BookScreen()
            1 -> CategoryScreen()
        }
    }
}

@Composable
fun CategoryScreen() {
    LazyColumn {
        items(10){
            Text(text = "Category $it")
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
@Composable
fun BookScreen() {
    LazyColumn {
        items(10){
            Text(text = "Book $it")
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val customCoroutine = rememberCoroutineScope()
    val tabNames =
        arrayOf(tabItem(Icons.Rounded.Book, "Books"), tabItem(Icons.Rounded.Category, "Categories"), )
    TabRow(selectedTabIndex = pagerState.currentPage) {
        tabNames.forEachIndexed { index, tabItem ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                customCoroutine.launch {
                    pagerState.animateScrollToPage(index)
                }
            }) {
                Row(
                    modifier = Modifier
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = tabItem.icon,
                        contentDescription = tabItem.title,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(text = tabItem.title, fontSize = 16.sp)
                }
            }
        }
    }
}


data class tabItem(
    val icon: ImageVector,
    val title: String
)
