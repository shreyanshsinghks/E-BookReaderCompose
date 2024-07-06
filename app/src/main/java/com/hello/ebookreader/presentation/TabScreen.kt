package com.hello.ebookreader.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hello.ebookreader.ui.theme.BackgroundColor
import com.hello.ebookreader.ui.theme.PrimaryColor
import com.hello.ebookreader.ui.theme.SurfaceColor
import com.hello.ebookreader.ui.theme.TextSecondaryColor
import kotlinx.coroutines.launch

@Composable
fun TabScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Tabs(pagerState = pagerState)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            AnimatedContent(
                targetState = page,
                transitionSpec = {
                    (fadeIn() + slideInHorizontally()).togetherWith(fadeOut() + slideOutHorizontally())
                }, label = ""
            ) { targetPage ->
                when (targetPage) {
                    0 -> AllBooksScreen(navController = navController)
                    1 -> CategoryScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Tabs(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val tabItems = listOf(
        TabItem(Icons.Rounded.Book, "Books"),
        TabItem(Icons.Rounded.Category, "Categories")
    )

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = SurfaceColor,
        contentColor = PrimaryColor,
        modifier = Modifier.shadow(4.dp)
    ) {
        tabItems.forEachIndexed { index, tabItem ->
            val selected = pagerState.currentPage == index
            Tab(
                selected = selected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                TabContent(tabItem, selected)
            }
        }
    }
}

@Composable
fun TabContent(tabItem: TabItem, selected: Boolean) {
    Row(
        modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = tabItem.icon,
            contentDescription = tabItem.title,
            tint = if (selected) PrimaryColor else TextSecondaryColor
        )
        AnimatedVisibility(visible = selected) {
            Text(
                text = tabItem.title,
                color = if (selected) PrimaryColor else TextSecondaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

data class TabItem(
    val icon: ImageVector,
    val title: String
)
