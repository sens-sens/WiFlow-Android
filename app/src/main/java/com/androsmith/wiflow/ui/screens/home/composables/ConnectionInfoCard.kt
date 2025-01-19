package com.androsmith.wiflow.ui.screens.home.composables

import android.content.ClipData
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
    modifier: Modifier = Modifier,
) {

    val clipboardManager = LocalClipboardManager.current

    val context = LocalContext.current


    Card(
        shape = RoundedCornerShape(
            topEnd = 32.dp,
            topStart = 32.dp,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
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
            Spacer(modifier = Modifier.height(28.dp))

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
                                    onClick =  {
                                        val clipData = ClipData.newPlainText("address", serverAddress)
                                        val clipEntry = ClipEntry(clipData)
                                        clipboardManager.setClip(clipEntry)

                                        Toast.makeText(context, "Copied server address", Toast.LENGTH_SHORT).show()

                                    }
                                    , modifier = Modifier.size(28.dp)


                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.copy),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)

                                    )
                                }
                            },

                        )
                    }

                    InfoTile(
                        title = "Username",
                        value = username,
                        action = {
                            IconButton(
                                onClick = {
                                    val clipData = ClipData.newPlainText("username", username)
                                    val clipEntry = ClipEntry(clipData)
                                    clipboardManager.setClip(clipEntry)
                                    Toast.makeText(context, "Copied username", Toast.LENGTH_SHORT).show()
                                }
                                , modifier = Modifier.size(28.dp)


                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.copy),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)

                                )
                            }
                        },

                    )


                    InfoTile(
                        title = "Password", value = password,
                    )

                    InfoTile(
                        title = "Directory", value = directory,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
            modifier = Modifier
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
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    action()
                }
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
        )
    }
}
