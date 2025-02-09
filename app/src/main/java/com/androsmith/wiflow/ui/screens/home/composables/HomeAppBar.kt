package com.androsmith.wiflow.ui.screens.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androsmith.wiflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,

    ) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.app_name),

            )
        },
        actions = {
            IconButton(
                onClick = onActionClick,
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.settings),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = stringResource(R.string.settings),
                    )
                }
            }
        },

        modifier = modifier
            .padding(horizontal = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun HomeAppBarPreview() {
    HomeAppBar(
        onActionClick = {}
    )
}