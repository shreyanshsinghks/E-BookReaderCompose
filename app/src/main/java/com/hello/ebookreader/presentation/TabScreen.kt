package com.hello.ebookreader.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hello.ebookreader.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun TabScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundColor)
    ) {
        Tabs(pagerState = pagerState)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            AnimatedContent(
                targetState = page,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
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
        containerColor = AppColors.SurfaceColor,
        contentColor = AppColors.PrimaryColor,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(4.dp),
        indicator = { tabPositions ->
            val indicator = SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .clip(RoundedCornerShape(16.dp)),
                height = 3.dp,
                color = AppColors.SecondaryColor
            )
        }
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
            tint = if (selected) AppColors.PrimaryColor else AppColors.TextSecondaryColor,
            modifier = Modifier.size(24.dp)
        )
        AnimatedVisibility(
            visible = selected,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Text(
                text = tabItem.title,
                color = if (selected) AppColors.PrimaryColor else AppColors.TextSecondaryColor,
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
