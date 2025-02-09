package com.androsmith.wiflow.ui.screens.welcome

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androsmith.wiflow.R
import com.androsmith.wiflow.ui.screens.home.composables.InfoTile
import com.androsmith.wiflow.ui.screens.home.composables.NeumorphicButton
import com.androsmith.wiflow.ui.screens.welcome.composables.BottomSection
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onNavigateToHome: () -> Unit, modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 5 })

    val coroutineScope = rememberCoroutineScope()

    val viewModel: WelcomeViewModel = viewModel(
        factory = WelcomeViewModel.Factory
    )


    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                BottomSection(
                    pageCount = pagerState.pageCount,
                    currentPage = pagerState.currentPage,
                    onBack = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage - 1,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                            )
                        }
                    },
                    onNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                            )
                        }
                    },
                    onFinish = {
                        viewModel.finishOnboarding()
                        onNavigateToHome()
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }) { padding ->
        HorizontalPager(

            state = pagerState,
            modifier = Modifier.padding(padding)
        ) { page ->
            when (page) {
                0 -> GreetingContent(
                    title = "Welcome to WiFlow!",
                    content = "Learn how to use the app. This guide will show you how to use the app effectively.",
                    image = R.drawable.logo,
                    )

                1 -> OnBoardingContent(
                    title = "Step 1: Choose a Directory",
                    instructions = listOf(
                        "To share files, select a folder from the Home screen.",
                        "Tap 'Choose Directory' to pick the folder you want to share.",
                        "You can update this folder anytime in Settings."
                    ),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            InfoTile(
                                title = "Directory", value = "/", action = {

                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.right_2_svgrepo_com),
                                            contentDescription = "Bullet point",
                                            modifier = Modifier.size(24.dp)

                                        )
                                        Spacer(

                                            modifier = Modifier
                                                .size(36.dp)
                                                .border(
                                                    width = 3.dp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                )

                                        )
                                    }

                                }, modifier = Modifier

                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onBackground,
                                        shape = RoundedCornerShape(
                                            8.dp
                                        )
                                    )
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                            Text("* Reference only",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5F),
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                                )
                        }
                    },
                )

                2 -> OnBoardingContent(
                    title = "Step 2: Start and Stop the Server",
                    instructions = listOf(
                        "Use the toggle button on the Home screen to start or stop the server.",
                        "When the server is running, you'll see details like FTP Address, Username and Password",
                        "Ensure your device is connected to the same Wi-Fi as the client."
                    ),
                    content = {
                        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp)) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ) {
                                NeumorphicButton(onClick = {},modifier = Modifier.padding(bottom = 20.dp).size(160.dp))


                            }
                            Text("* Reference only",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5F),
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
)

                        }
                    },
                )

                3 -> OnBoardingContent(
                    title = "Step 3: Use an FTP Client",
                    instructions = listOf(
                        "Open an FTP client (e.g., Windows Explorer).",
                        "Enter the FTP address in this format: ftp://<FTP_ADDRESS>.",
                        "Use the username and password shown on the Home screen.",
                        "Start transferring files!"
                    ),
                    content = {
                        Image(
                            painter = painterResource(R.drawable.ftp_client),
                            contentDescription = "FTP Client application screenshot",
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier

                                .height(152.dp)


                                .clip(
                                    shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .4f),
                                    shape = RoundedCornerShape(
                                        8.dp
                                    )
                                )


                        )
                    },
                )

                4 -> GreetingContent(
                    title = "You're Ready to Start!",
                    content = "Follow these steps anytime to share files with WiFlow.",
                    image = R.drawable.logo,

                    )

                else -> Text("Unknown")
            }
        }
    }
}


@Composable
fun GreetingContent(
    title: String, content: String, @DrawableRes image: Int, modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image), contentDescription = "App logo"
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = title, style = TextStyle(
                fontSize = 28.sp,

                ), textAlign = TextAlign.Center


        )


        Spacer(modifier = Modifier.height(36.dp))

        Text(
            content, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnBoardingContent(
    title: String,
    instructions: List<String>,
    content: @Composable() () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 28.dp, horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title, style = TextStyle(
                fontSize = 28.sp,
                lineHeight = 40.sp
            ), textAlign = TextAlign.Center


        )
        Spacer(modifier = Modifier.height(48.dp))
        Box(
            modifier = Modifier.height(180.dp), contentAlignment = Alignment.Center
        ) {
            content()
        }
        Spacer(modifier = Modifier.height(48.dp))

        LazyColumn {
            items(instructions) { instruction ->
                Row(
                    modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top
                ) {
                    Text(
                        ">",

                        modifier = Modifier.size(20.dp)
                    )
//                    Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    Text(
                        instruction,
                    )
                }
            }
        }
    }
}