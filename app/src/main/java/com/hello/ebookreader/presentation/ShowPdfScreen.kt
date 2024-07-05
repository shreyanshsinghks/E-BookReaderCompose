package com.hello.ebookreader.presentation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

@Composable
fun ShowPdfScreen(url: String) {
    val pdfState = rememberVerticalPdfReaderState(resource =
        ResourceType.Remote(url),
        isZoomEnable = true,
    )

    VerticalPDFReader(state = pdfState, modifier = Modifier.background(Color.Gray))
}