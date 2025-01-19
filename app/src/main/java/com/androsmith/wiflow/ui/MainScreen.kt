package com.androsmith.wiflow.ui
//
//import android.net.Uri
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.androsmith.wiflow.utils.StorageUtils
//
//@Composable
//fun MainScreen(context: ComponentActivity) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(onClick = { StorageUtils.openDirectoryChooser(directoryChooserLauncher) }) {
//            Text("Choose Directory")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(text = directoryPath)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = {
//            directoryUri?.let {
//                StorageUtils.createFileInDirectory(context, it, "my_new_file.txt", "Hello from SAF!")
//            }
//        }, enabled = isWriteEnabled) {
//            Text("Write File")
//        }
//    }
//}