package com.androsmith.wiflow.ui.screens.welcome.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomSection(
    pageCount: Int,
    currentPage: Int,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        BackButton(
            onBack = {
                if (currentPage != 0) {
                    onBack()
                }
            },
            enabled = currentPage != 0,
            modifier = Modifier.weight(1F)

        )

        PageIndicator(
            pageCount = pageCount, currentPage = currentPage,             modifier = Modifier.weight(1F)

        )

        NextButton(
            onNext = {
                if (currentPage != pageCount -1) {
                    onNext()
                }
            } ,
            onFinish = onFinish,
            enabled = currentPage != pageCount -1,
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
fun BackButton(
    onBack: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true
) {
    val textColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colorScheme.primary else  Color.Transparent
    )
    TextButton(
        onClick = onBack, modifier = modifier

    ) {
        Text("Back", style = TextStyle(color =textColor))
    }
}

@Composable
fun NextButton(
    onNext: () -> Unit, onFinish: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true,
) {
    if (enabled) {
        TextButton(
            onClick = onNext, modifier = modifier
        ) {
            Text("Next")
        }
    } else {
        TextButton(
            onClick = onFinish, modifier = modifier
        ) {
            Text("Go to App")
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int, currentPage: Int, modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) {
            val isActive = currentPage  == it

            val indicatorWidth by animateDpAsState(targetValue =if (isActive) 16.dp else 8.dp)
            val indicatorColor by animateColorAsState(
                targetValue = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
            )

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .height(8.dp)
                    .width(indicatorWidth)
                    .clip(
                        shape = CircleShape
                    )
                    .background(
                        color = indicatorColor
                    )


            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSectionPreview() {
    BottomSection(
        pageCount = 5,
        currentPage = 2,
        onNext = {},
        onBack = {},
        onFinish = {}
    )
}