package com.androsmith.wiflow.ui.screens.home


import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.androsmith.wiflow.ui.screens.settings.LaunchIntent
import com.androsmith.wiflow.ui.screens.settings.LaunchIntent.ManageStorageRequest
import com.androsmith.wiflow.ui.screens.settings.LaunchIntent.PermissionRequest
import com.androsmith.wiflow.ui.screens.settings.SettingsViewModel
import com.androsmith.wiflow.ui.theme.WiFlowTheme


@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory
    )

    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory
    )
    val context = LocalContext.current

    val manageStorageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.handleManageStorageResult(result.resultCode, context)
        }

    val storagePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            viewModel.handleStoragePermissionResult(permissions)
        }

    val openDirectoryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.handleDirectorySelectionResult(
                result.data?.data,
                context.contentResolver,
                context
            )
        }

    val launchIntent by viewModel.launchIntent.collectAsState() // Use collectAsState()

    LaunchedEffect(launchIntent) {
        viewModel.launchIntent.value?.let { intent ->
            when (intent) {
                is LaunchIntent.OpenDirectoryIntent -> openDirectoryLauncher.launch(intent.intent)
                is PermissionRequest -> storagePermissionLauncher.launch(intent.permissions)
                is ManageStorageRequest -> manageStorageLauncher.launch(intent.intent)
            }
            viewModel.onIntentLaunched()
        }
    }


    val uiState = homeViewModel.uiState.collectAsState().value



    Scaffold(
        topBar = {
            HomeAppBar(
                onActionClick = onNavigateToSettings
            )
                 },
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

                    homeViewModel.toggleServer(context)
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
                ),
                onChooseDirectory = { viewModel.chooseDirectory(context) }


            )

            homeViewModel.toastMessage.collectAsState().value?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                homeViewModel.clearToast()
            }

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
