package com.androsmith.wiflow.ui.screens.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androsmith.wiflow.R
import com.androsmith.wiflow.ui.screens.settings.composables.SettingsAppBar

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onInstructionPageClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                is LaunchIntent.PermissionRequest -> storagePermissionLauncher.launch(intent.permissions)
                is LaunchIntent.ManageStorageRequest -> manageStorageLauncher.launch(intent.intent)
            }
            viewModel.onIntentLaunched()
        }
    }

    val usernameDialog = remember { mutableStateOf(false) }
    val passwordDialog = remember { mutableStateOf(false) }
    val deviceNameDialog = remember { mutableStateOf(false) }

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val deviceName by viewModel.deviceName.collectAsState()




    Scaffold(
        topBar = {
            SettingsAppBar(
                onNavigateBack = onNavigateBack
            )
        },
        modifier = modifier,
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding)
        ) {

            Spacer(Modifier.height(16.dp))


            EditTextDialog(
                showDialog = usernameDialog.value,
                label = stringResource(R.string.username_label),
                currentValue = username,
                onDismissRequest = { usernameDialog.value = false },
                onValueChange = { newUsername ->
                    viewModel.changeUsername(newUsername)
                }
            )

            EditTextDialog(
                showDialog = passwordDialog.value,
                label = stringResource(R.string.password_label),
                currentValue = password,
                onDismissRequest = { passwordDialog.value = false },
                onValueChange = { newPassword ->
                    viewModel.changePassword(newPassword)
                }
            )

            EditTextDialog(
                showDialog = deviceNameDialog.value,
                label = stringResource(R.string.device_name),
                currentValue = deviceName,
                onDismissRequest = { deviceNameDialog.value = false },
                onValueChange = { newName ->
                    viewModel.changeDeviceName(newName)
                }
            )


            SettingsTile(
                title = stringResource(R.string.username),
                value = stringResource(R.string.username_desc),
                onClick = { usernameDialog.value = true }
            )

            SettingsTile(
                title = stringResource(R.string.password),
                value = stringResource(R.string.password_desc),
                onClick = { passwordDialog.value = true }
            )

            SettingsTile(
                title = stringResource(R.string.device_name),
                value = stringResource(R.string.device_name_desc),
                onClick = { deviceNameDialog.value = true }
            )

            SettingsTile(
                title = stringResource(R.string.directory),
                value = stringResource(R.string.directory_desc),
                onClick = { viewModel.chooseDirectory(context) }
            )
            SettingsTile(
                title = stringResource(R.string.instructions),
                value = stringResource(R.string.instructions_desc),
                onClick = onInstructionPageClicked
            )


            viewModel.toastMessage.collectAsState().value?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearToast()
            }
        }
    }
}


@Composable
fun SettingsTile(

    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick ?: {}
            )
            .padding(vertical = 16.dp, horizontal = 28.dp),

        ) {
        Text(
            title, style = TextStyle(
                fontSize = 17.sp, color = MaterialTheme.colorScheme.inverseSurface
            )

        )
        Spacer(Modifier.height(10.dp))
        Text(
            value, style = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
            )
        )
        Spacer(Modifier.height(10.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
        )
    }


}


@Composable
fun EditTextDialog(
    showDialog: Boolean,
    currentValue: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.unknown),
) {
    if (showDialog) {
        var newValue by remember { mutableStateOf(currentValue) }

        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.edit_label, label),
                        style = TextStyle(
                            fontSize = 20.sp,
                        ),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newValue,
                        onValueChange = { newValue = it },
                        label = { Text(stringResource(R.string.new_label, label)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text(stringResource(R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onValueChange(newValue)
                            onDismissRequest()
                        }) {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
    }
}
