package com.hello.ebookreader.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hello.ebookreader.ui.theme.AccentColor1
import com.hello.ebookreader.ui.theme.BackgroundColor
import com.hello.ebookreader.ui.theme.NightModeBackground
import com.hello.ebookreader.ui.theme.NightModeText
import com.hello.ebookreader.ui.theme.OnPrimaryColor
import com.hello.ebookreader.ui.theme.PrimaryColor
import com.hello.ebookreader.ui.theme.TextPrimaryColor
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPdfScreen(url: String, bookName: String, navController: NavController) {
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Remote(url),
        isZoomEnable = true,
    )
    var showControls by remember { mutableStateOf(true) }
    var isNightMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showControls,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = bookName,
                            color = OnPrimaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = OnPrimaryColor)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isNightMode = !isNightMode }) {
                            Icon(
                                if (isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Night Mode",
                                tint = OnPrimaryColor
                            )
                        }
                    },
                    colors = topAppBarColors(containerColor = PrimaryColor)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isNightMode) NightModeBackground else BackgroundColor)
        ) {
            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier.fillMaxSize()
            )

            // Loading indicator
            if (!pdfState.isLoaded) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
            }

            // Page number indicator and controls
            AnimatedVisibility(
                visible = showControls,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {

                        },
                        enabled = pdfState.currentPage > 0
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Page",
                            tint = if (isNightMode) NightModeText else TextPrimaryColor
                        )
                    }
                    Text(
                        text = "Page ${pdfState.currentPage + 1} of ${pdfState.pdfPageCount}",
                        color = if (isNightMode) NightModeText else TextPrimaryColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = {

                        },
                        enabled = pdfState.currentPage < pdfState.pdfPageCount - 1
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Page",
                            tint = if (isNightMode) NightModeText else TextPrimaryColor
                        )
                    }
                }
            }

            // Progress indicator
            LinearProgressIndicator(
                progress = { pdfState.loadPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.TopCenter),
                color = AccentColor1,
                trackColor = Color.Transparent,
            )
        }
    }

    // Toggle controls visibility on tap
    LaunchedEffect(pdfState.currentPage) {
        showControls = true
        delay(3000)
        showControls = false
    }
}