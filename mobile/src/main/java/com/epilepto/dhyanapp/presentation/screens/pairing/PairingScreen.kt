//package com.epilepto.dhayan.presentation.screens.pairing
//
//import android.content.pm.PackageManager
//import android.os.Build
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Refresh
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ElevatedCard
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.epilepto.dhayan.data.bluetooth.model.BluetoothDeviceDTO
//import com.epilepto.dhayan.R
//import com.epilepto.dhyanapp.presentation.components.LoadingScreen
//import com.google.android.horologist.annotations.ExperimentalHorologistApi
//import com.google.android.horologist.data.apphelper.AppHelperNodeStatus
//import com.google.android.horologist.datalayer.phone.PhoneDataLayerAppHelper
//import com.stevdzasan.messagebar.ContentWithMessageBar
//import com.stevdzasan.messagebar.MessageBarState
//import kotlinx.coroutines.launch
//
//@Composable
//fun PairingScreen(
//    messageBarState: MessageBarState,
//    uiState: PairingViewModel.BluetoothUiState,
//    startScan: () -> Unit,
//    restartScan: () -> Unit,
//    onDeviceClick: (BluetoothDeviceDTO) -> Unit,
//    navigateToPermissions: () -> Unit,
//    startServer: () -> Unit,
//) {
//    val context = LocalContext.current
//    LaunchedEffect(key1 = Unit) {
//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//                if (
//                    (context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN)
//                            == PackageManager.PERMISSION_GRANTED &&
//                            context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
//                            == PackageManager.PERMISSION_GRANTED)
//
//                ) {
//                    startScan()
//                } else {
//                    navigateToPermissions()
//                }
//            }
//
//            else -> {
//                if (
//                    context.checkSelfPermission(
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) == PackageManager.PERMISSION_GRANTED &&
//
//                    context.checkSelfPermission(
//                        android.Manifest.permission.ACCESS_FINE_LOCATION
//                    ) == PackageManager.PERMISSION_GRANTED
//
//                ) {
//                    startScan()
//                } else {
//                    navigateToPermissions()
//                }
//            }
//
//        }
//
//    }
//
//    LoadingScreen(
//        loadingState = uiState.isConnecting,
//        modifier = Modifier.fillMaxSize()
//    )
//
//    LaunchedEffect(key1 = uiState.error) {
//        if (!uiState.error.isNullOrEmpty()) {
//            messageBarState.addError(Exception(uiState.error))
//        }
//    }
//
//    ContentWithMessageBar(messageBarState = messageBarState) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Column(
//                modifier = Modifier.weight(.5f)
//            ) {
//                Text(
//                    text = "Paired Devices",
//                    style = MaterialTheme.typography.titleLarge,
//                    modifier = Modifier.padding(12.dp)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//                if (uiState.pairedDevices.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = "No paired devices yet.")
//                    }
//                } else
//                    LazyColumn {
//                        items(
//                            items = uiState.pairedDevices,
//                            key = { item ->
//                                item.address
//                            }
//                        ) { pairedDevice ->
//                            Card(
//                                modifier = Modifier
//                                    .padding(
//                                        horizontal = 12.dp,
//                                        vertical = 6.dp
//                                    )
//                            ) {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Column {
//                                        Text(text = pairedDevice.name ?: "Unknown")
//                                        Spacer(modifier = Modifier.padding(6.dp))
//                                        Text(text = pairedDevice.address)
//                                    }
//                                }
//                            }
//                        }
//                    }
//            }
//
//            Spacer(modifier = Modifier.padding(12.dp))
//
//            Column(
//                modifier = Modifier.weight(.5f)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Nearby Devices (Click here to start server)",
//                        style = MaterialTheme.typography.titleLarge,
//                        modifier = Modifier
//                            .padding(12.dp)
//                            .clickable {
//                                startServer()
//                                Toast
//                                    .makeText(
//                                        context,
//                                        "Server started",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                    .show()
//                            }
//                    )
//
//                    IconButton(onClick = restartScan) {
//                        Icon(
//                            imageVector = Icons.Outlined.Refresh,
//                            contentDescription = "refresh"
//                        )
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//                if (uiState.scannedDevices.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .weight(1f),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                } else
//                    LazyColumn {
//
//                        items(
//                            items = uiState.scannedDevices,
//                            key = { item ->
//                                item.address
//                            }) { pairedDevice ->
//                            Card(
//                                modifier = Modifier
//                                    .padding(
//                                        horizontal = 12.dp,
//                                        vertical = 6.dp
//                                    )
//                                    .clickable {
//                                        onDeviceClick(pairedDevice)
//                                    }
//                            ) {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Column {
//                                        Text(text = pairedDevice.name ?: "Unknown")
//                                        Spacer(modifier = Modifier.padding(6.dp))
//                                        Text(text = pairedDevice.address)
//                                    }
//                                }
//                            }
//                        }
//                    }
//            }
//        }
//    }
//}
//
//@Composable
//fun PairedScreen() {
//    Image(
//        painter = painterResource(id = R.drawable.mindfullness_ui_bg),
//        contentDescription = "background",
//        modifier = Modifier.fillMaxSize(),
//        contentScale = ContentScale.Crop
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.TopCenter)
//                .padding(12.dp),
//            horizontalAlignment = Alignment.Start
//        ) {
//            Spacer(modifier = Modifier.padding(24.dp))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.calm_emoji),
//                    contentDescription = "calm",
//                    modifier = Modifier.padding(end = 4.dp)
//                )
//                Text(
//                    text = "Dhyaan",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.LightGray,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//            Text(
//                text = "Pairing",
//                fontSize = MaterialTheme.typography.displayMedium.fontSize,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//        }
//
//        Column(
//            modifier = Modifier.align(Alignment.Center),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.watch_icon),
//                contentDescription = "watch",
//                modifier = Modifier
//                    .padding(20.dp)
//                    .scale(1.4f)
//            )
//            Spacer(modifier = Modifier.padding(24.dp))
//
//            ElevatedCard(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                colors = CardDefaults.elevatedCardColors(
//                    containerColor = Color.White
//                )
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "Google WearOS Watch",
//                        style = TextStyle(
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
//                            color = Color.Black
//                        ),
//                        modifier = Modifier.padding(24.dp),
//                        textAlign = TextAlign.Center
//                    )
//
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalHorologistApi::class)
//@Composable
//fun HorologistPairingScreen(
//    phoneDataLayerAppHelper: PhoneDataLayerAppHelper,
//    onSend: (String,String) -> Unit
//) {
//    var isAvailable by remember {
//        mutableStateOf(false)
//    }
//    val coroutineScope = rememberCoroutineScope()
//    var nodeList by remember { mutableStateOf<List<AppHelperNodeStatus>>(emptyList()) }
//
//    LaunchedEffect(key1 = Unit){
//        isAvailable = phoneDataLayerAppHelper.isAvailable()
//    }
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            nodeList = if (
//                phoneDataLayerAppHelper.isAvailable()
//            ) phoneDataLayerAppHelper.connectedNodes() else listOf()
//        }
//    }
//
//
//
//
//    MainScreen(
//        apiAvailable = isAvailable,
//        nodeList = nodeList,
//        onListNodes = {
//            coroutineScope.launch {
//                nodeList = phoneDataLayerAppHelper.connectedNodes()
//            }
//        },
//        onLaunchClick = { nodeId ->
//            coroutineScope.launch {
//                phoneDataLayerAppHelper.startRemoteOwnApp(nodeId)
//            }
//        },
//        onInstallClick = { nodeId ->
//            coroutineScope.launch {
//                phoneDataLayerAppHelper.installOnNode(nodeId)
//            }
//        },
//        onSend = onSend
//    )
//}
//
//@Composable
//fun MainScreen(
//    apiAvailable: Boolean,
//    nodeList: List<AppHelperNodeStatus>,
//    onListNodes: () -> Unit,
//    onInstallClick: (String) -> Unit,
//    onLaunchClick: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    onSend: (String, String) -> Unit
//) {
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        val (message, onMessageChanged) = rememberSaveable { mutableStateOf("") }
//
//        TextField(value = message, onValueChange = onMessageChanged)
//
//        Button(onClick = {
//            if (message.isNotEmpty()) {
//                onSend(nodeList[0].id, message)
//            }
//        }) {
//            Text(text = "Send Message")
//        }
//
//        Button(
//            onClick = {
//                onListNodes()
//            },
//            modifier = Modifier.wrapContentHeight(),
//            enabled = apiAvailable,
//        ) { Text("List Nodes") }
//
//        nodeList.forEach { nodeStatus ->
//            AppHelperNodeStatusCard(
//                nodeStatus = nodeStatus,
//                onInstallClick = onInstallClick,
//                onLaunchClick = onLaunchClick
//            )
//        }
//
//        if (!apiAvailable) {
//            Text(
//                text = "API Not Available",
//                modifier.fillMaxWidth(),
//                color = Color.Red,
//                textAlign = TextAlign.Center,
//            )
//        }
//
//    }
//}
//
//@Composable
//fun AppHelperNodeStatusCard(
//    nodeStatus: AppHelperNodeStatus,
//    onInstallClick: (String) -> Unit,
//    onLaunchClick: (String) -> Unit,
//) {
//    Box(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth(),
//        contentAlignment = Alignment.Center,
//    ) {
//        Card {
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth(),
//
//                ) {
//                Text(nodeStatus.displayName)
//                Text(
//                    style = MaterialTheme.typography.labelMedium,
//                    text = nodeStatus.id,
//                )
//                Text(
//                    style = MaterialTheme.typography.labelMedium,
//                    text = nodeStatus.nodeType.name,
//                )
//                Text(
//                    style = MaterialTheme.typography.labelMedium,
//                    text = nodeStatus.isAppInstalled.toString()
//                )
//                if (nodeStatus.surfacesInfo.complicationsList.isNotEmpty()) {
//                    Text(
//                        style = MaterialTheme.typography.labelMedium,
//                        text = nodeStatus.surfacesInfo.complicationsList.joinToString { it.name }
//                    )
//                }
//                if (nodeStatus.surfacesInfo.tilesList.isNotEmpty()) {
//                    Text(
//                        style = MaterialTheme.typography.labelMedium,
//                        text = nodeStatus.surfacesInfo.tilesList.joinToString { it.name }
//                    )
//                }
//                Text(
//                    style = MaterialTheme.typography.labelMedium,
//                    text = nodeStatus.surfacesInfo.usageInfo.usageStatus.name,
//                )
//                Row(
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    Button(
//                        modifier = Modifier.wrapContentHeight(),
//                        onClick = { onInstallClick(nodeStatus.id) },
//                    ) {
//                        Text("Install")
//                    }
//                    Button(
//                        modifier = Modifier.wrapContentHeight(),
//                        onClick = { onLaunchClick(nodeStatus.id) },
//                    ) {
//                        Text("Launch")
//                    }
//                }
//            }
//        }
//    }
//}
