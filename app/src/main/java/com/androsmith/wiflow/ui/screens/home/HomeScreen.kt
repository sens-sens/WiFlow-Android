package com.androsmith.wiflow.ui.screens.home


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androsmith.wiflow.ui.screens.home.composables.ConnectionInfoCard
import com.androsmith.wiflow.ui.screens.home.composables.HomeAppBar
import com.androsmith.wiflow.ui.screens.home.composables.NeumorphicButton
import com.androsmith.wiflow.ui.screens.home.composables.StatusIndicator
import com.androsmith.wiflow.ui.theme.WiFlowTheme


@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    )

    val uiState = viewModel.uiState.collectAsState().value





    val context = LocalContext.current

    Scaffold(
        topBar = { HomeAppBar(
            onActionClick = onNavigateToSettings
        ) },
        modifier = modifier,
    ) { padding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(padding),
        ) {

            StatusIndicator(
                enabled = uiState.isRunning,
                modifier = Modifier.padding(vertical = 28.dp),
            )

            NeumorphicButton(
                onClick = {

                    viewModel.toggleServer(context)
                          },
                pressed = uiState.isRunning,
                modifier = Modifier.padding(bottom = 56.dp)
            )

            Spacer(Modifier.weight(1f))

            ConnectionInfoCard(

                username = uiState.config.username,
                password = uiState.config.password,
                serverAddress = uiState.address,
                isRunning = uiState.isRunning,
                directory = uiState.config.rootDirectory.replace(
                    "/storage/emulated/0",""
                )
            )

            viewModel.toastMessage.collectAsState().value?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WiFlowTheme() {
        HomeScreen(
            onNavigateToSettings = {}
        )
    }
}
