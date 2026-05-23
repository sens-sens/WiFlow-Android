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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.res.stringResource
import com.androsmith.wiflow.R
import com.androsmith.wiflow.domain.FtpServerState

@Composable
fun StatusIndicator(
    serverState: FtpServerState,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val backgroundColor = when (serverState) {
        is FtpServerState.Running -> MaterialTheme.colorScheme.tertiaryContainer
        FtpServerState.Stopped -> MaterialTheme.colorScheme.errorContainer
        FtpServerState.Starting, FtpServerState.Stopping -> if (isDark) Color(0xFF663300) else Color(0xFFFFE0B2)
    }

    val contentColor = when (serverState) {
        is FtpServerState.Running -> MaterialTheme.colorScheme.onTertiaryContainer
        FtpServerState.Stopped -> MaterialTheme.colorScheme.onErrorContainer
        FtpServerState.Starting, FtpServerState.Stopping -> if (isDark) Color(0xFFFFCC80) else Color(0xFFE65100)
    }

    val statusText = when (serverState) {
        is FtpServerState.Running -> stringResource(R.string.server_running)
        FtpServerState.Stopped -> stringResource(R.string.server_stopped)
        FtpServerState.Starting -> stringResource(R.string.server_starting)
        FtpServerState.Stopping -> stringResource(R.string.server_stopping)
    }

    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = backgroundColor)
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(shape = CircleShape)
                    .background(color = contentColor)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = statusText,
                color = contentColor
            )
        }
    }
}

@Composable
fun StatusChip() {

}