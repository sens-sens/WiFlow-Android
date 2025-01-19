package com.androsmith.wiflow.ui.screens.settings.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androsmith.wiflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
            ) {
                Box(

                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = stringResource(R.string.settings),
                    )
                }
            }
        },
        title = {
            Text(
                "Settings",
                color = MaterialTheme.colorScheme.onBackground,
            )
        },


        modifier = modifier
            .padding(horizontal = 8.dp),
    )
}

