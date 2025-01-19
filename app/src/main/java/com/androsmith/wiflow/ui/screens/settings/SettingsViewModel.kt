package com.androsmith.wiflow.ui.screens.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.androsmith.wiflow.WiFlowApplication
import com.androsmith.wiflow.data.UserPreferencesRepository
import com.androsmith.wiflow.ui.screens.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class LaunchIntent {
    data class PermissionRequest(val permissions: Array<String>) : LaunchIntent()
    data class ManageStorageRequest(val intent: Intent) : LaunchIntent()
    data class OpenDirectoryIntent(val intent: Intent) : LaunchIntent()
}


class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository

) : ViewModel() {

    private val _chosenDirectoryUri = MutableStateFlow<String?>(null)
    val chosenDirectoryUri: StateFlow<String?> = _chosenDirectoryUri

    private val _chosenDirectoryName = MutableStateFlow<String?>(null)
    val chosenDirectoryName: StateFlow<String?> = _chosenDirectoryName

    private val _launchIntent = MutableStateFlow<LaunchIntent?>(null)
    val launchIntent: StateFlow<LaunchIntent?> = _launchIntent

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    fun changePassword(value: String) {
        _password.value = value
        viewModelScope.launch {
            userPreferencesRepository.updatePassword(value)
        }
    }
    fun changeUsername(value: String) {
        _username.value = value
        viewModelScope.launch {
            userPreferencesRepository.updateUsername(value)
        }
    }

    fun chooseDirectory(context: android.content.Context) {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    _launchIntent.value =
                        LaunchIntent.OpenDirectoryIntent(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
                } else {
                    try {
                        val uri = Uri.parse("package:" + context.packageName)
                        val intent =
                            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                        _launchIntent.value = LaunchIntent.ManageStorageRequest(intent)
                    } catch (e: Exception) {
                        // Handle the case where the device doesn't support the direct intent
                        // Fallback to the general manage all files access permission
                        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        _launchIntent.value = LaunchIntent.ManageStorageRequest(intent)
                    }
                }
            } else {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (permissions.all {
                        ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
                    }) {
                    _launchIntent.value =
                        LaunchIntent.OpenDirectoryIntent(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
                } else {
                    _launchIntent.value = LaunchIntent.PermissionRequest(permissions)
                }
            }
        }
    }

    fun handleManageStorageResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            _launchIntent.value =
                LaunchIntent.OpenDirectoryIntent(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        }
    }

    fun handleStoragePermissionResult(permissions: Map<String, Boolean>) {
        if (permissions.all { it.value }) {
            _launchIntent.value =
                LaunchIntent.OpenDirectoryIntent(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        } else {
            _toastMessage.value = "Storage Permission Denied"
        }
    }

    fun handleDirectorySelectionResult(
        uri: Uri?,
        contentResolver: android.content.ContentResolver,
        context: android.content.Context
    ) { // Add context parameter
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            val folderPath = getFolderPath(it)
            _chosenDirectoryUri.value = folderPath
            if (folderPath != null) {
                viewModelScope.launch {
                    userPreferencesRepository.updateDirectory(folderPath)
                }
            }
            val docFile = DocumentFile.fromTreeUri(context, it) // Use the passed context
            _chosenDirectoryName.value = docFile?.name
        }
    }


    fun onIntentLaunched() {
        _launchIntent.value = null
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun getFolderPath(uri: Uri): String? {
        val docId = DocumentsContract.getTreeDocumentId(uri)
        val split = docId.split(":")
        val type = split[0]
        val path = split.getOrNull(1)
        return when (type) {
            "primary" -> "/storage/emulated/0/$path"
            else -> null // Handle other storage types if needed

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as WiFlowApplication)
                SettingsViewModel(application.container.userPreferencesRepository)
            }
        }
    }
}