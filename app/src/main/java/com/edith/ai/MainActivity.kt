
package com.edith.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Dark = Color(0xFF151515)
private val Orange = Color(0xFFB95727)
private val White = Color(0xFFF5F5F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EdithApp() }
    }
}

@Composable
fun EdithApp() {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<String>()) }
    var selectedPage by remember { mutableStateOf("Chat") }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(290.dp).fillMaxHeight(),
                    drawerContainerColor = Dark
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        Text("Edith", color = White, fontSize = 26.sp)
                        Spacer(Modifier.height(24.dp))

                        listOf(
                            "Chat",
                            "Models",
                            "Free API Directory",
                            "Plugins",
                            "Usage"
                        ).forEach { page ->
                            NavigationDrawerItem(
                                label = { Text(page) },
                                selected = selectedPage == page,
                                onClick = {
                                    selectedPage = page
                                    scope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = Color(0xFF292929),
                                    unselectedContainerColor = Dark,
                                    selectedTextColor = White,
                                    unselectedTextColor = White
                                )
                            )
                        }

                        Spacer(Modifier.weight(1f))
                        HorizontalDivider(color = Color(0xFF444444))
                        Spacer(Modifier.height(18.dp))

                        NavigationDrawerItem(
                            label = { Text("Settings") },
                            selected = selectedPage == "Settings",
                            onClick = {
                                selectedPage = "Settings"
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF292929),
                                unselectedContainerColor = Dark,
                                selectedTextColor = White,
                                unselectedTextColor = White
                            )
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(Dark)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "☰",
                        color = White,
                        fontSize = 26.sp,
                        modifier = Modifier.clickable {
                            scope.launch { drawerState.open() }
                        }.padding(8.dp)
                    )

                    Text(selectedPage, color = White, fontSize = 24.sp)

                    Text(
                        "＋",
                        color = Orange,
                        fontSize = 28.sp,
                        modifier = Modifier.clickable {
                            messages = emptyList()
                            message = ""
                            selectedPage = "Chat"
                        }.padding(8.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                if (messages.isEmpty()) {
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Hello!", color = White, fontSize = 30.sp)
                        Text(
                            "How can I help you today?",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(messages) { item ->
                            Text(
                                item,
                                color = White,
                                modifier = Modifier.fillMaxWidth()
                                    .background(
                                        Color(0xFF292929),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(14.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            Color(0xFF292929),
                            RoundedCornerShape(28.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("＋", color = Orange, fontSize = 24.sp)

                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Message Edith...") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    TextButton(
                        onClick = {
                            if (message.isNotBlank()) {
                                messages = messages + message
                                message = ""
                            }
                        }
                    ) {
                        Text("➤", color = Orange, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}
