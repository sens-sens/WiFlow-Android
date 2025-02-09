package com.androsmith.wiflow.ui.screens.home.composables

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androsmith.wiflow.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConnectionInfoCard(

    serverAddress: String,
    directory: String,
    username: String,
    password: String,
    isRunning: Boolean,
    onChooseDirectory: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val clipboardManager = LocalClipboardManager.current

    val context = LocalContext.current

    var isHidden = remember { mutableStateOf(true) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .offset(y=(-16).dp)

            .border(
                2.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = .04F),
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp,)
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()


        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                    )

                    .padding(
                        horizontal = 28.dp,
                    ),
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    stringResource(R.string.connection_info),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f),
                    ),
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f),
                    modifier = Modifier
                        .width(155.dp)
                        .padding(top = 4.dp),
                )

                Spacer(modifier = Modifier.height(28.dp))

                CompositionLocalProvider(
                    value = LocalOverscrollConfiguration provides null,
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState(),
                        ),
                    ) {


                        AnimatedVisibility(
                            visible = isRunning
                        ) {
                            InfoTile(
                                title = "Address",
                                value = serverAddress,
                                action = {
                                    IconButton(
                                        onClick = {
                                            val clipData =
                                                ClipData.newPlainText("address", serverAddress)
                                            val clipEntry = ClipEntry(clipData)
                                            clipboardManager.setClip(clipEntry)

                                            Toast.makeText(
                                                context, "Copied server address", Toast.LENGTH_SHORT
                                            ).show()

                                        },


                                        ) {
                                        Icon(
                                            painter = painterResource(R.drawable.copy),
                                            contentDescription = "Copy server address",
                                            modifier = Modifier.size(22.dp)

                                        )
                                    }
                                },

                                )
                        }

                        InfoTile(
                            title = "Username",
                            value = username,


                            )



                        InfoTile(
                            title = "Password",
                            value = if (isHidden.value) "* * * * * * *" else password,
                            action = {
                                IconButton(
                                    onClick = {
                                        isHidden.value = !isHidden.value
                                    },

                                    ) {
                                    Icon(
                                        if (isHidden.value) painterResource(R.drawable.eye_closed_svgrepo_com)
                                        else painterResource(R.drawable.eye_open_svgrepo_com),
                                        contentDescription = if (isHidden.value) "Show password" else "Hide passwodr",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                            }
                        )

                        InfoTile(
                            title = "Directory", value = directory,
                            action = {
                                IconButton(
                                    onClick = {
                                        onChooseDirectory()
                                    },


                                    ) {
                                    Icon(
                                        painter = painterResource(R.drawable.right_2_svgrepo_com),
                                        contentDescription = "Choose directory",
                                        modifier = Modifier
                                            .size(24.dp)

                                    )
                                }
                            },
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                    }
                }
            }
        }
    }
}


@Composable
fun InfoTile(

    title: String,
    value: String,
    modifier: Modifier = Modifier,
    action: (@Composable() () -> Unit)? = null,
) {
    Column {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    title, style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
                    )
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    value, style = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colorScheme.inverseSurface
                    )
                )

            }
            Spacer(Modifier.weight(1f))
            if (action != null) {

                action()

            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoTilePreview() {
    InfoTile(
        title = "Directory",
        value = "/Documents",
        action = {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                IconButton(
                    onClick = {},


                    ) {
                    Icon(
                        painterResource(R.drawable.right_2_svgrepo_com),
                        contentDescription = "Choose directory",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    )

}