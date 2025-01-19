package com.androsmith.wiflow.ui.screens.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicator(
    enabled: Boolean,
    modifier: Modifier = Modifier
) {




    if (enabled) {
        Box(
            modifier = modifier
                .clip(
                    shape = CircleShape
                )
                .background(
                    color = MaterialTheme
                        .colorScheme
                        .tertiaryContainer
                ).
                padding(vertical = 12.dp, horizontal = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(8.dp)

                        .clip(
                            shape = CircleShape
                        )
                        .background(
                            color = MaterialTheme
                                .colorScheme
                                .onTertiaryContainer
                        )
                )
                Spacer(Modifier.width(8.dp))
                Text("Server Running",
                    color = MaterialTheme
                        .colorScheme
                        .onTertiaryContainer
                )
            }
        }
    } else {
            Box(
                modifier = modifier
                    .clip(
                        shape = CircleShape
                    )
                    .background(
                        color = MaterialTheme
                            .colorScheme
                            .errorContainer
                    ).
                    padding(vertical = 12.dp, horizontal = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(8.dp)

                            .clip(
                                shape = CircleShape
                            )
                            .background(
                                color = MaterialTheme
                                    .colorScheme
                                    .onErrorContainer
                            )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Server Stopped",
                        color = MaterialTheme
                            .colorScheme
                            .onErrorContainer
                    )
                }
    }

    }
}

@Composable
fun StatusChip() {

}