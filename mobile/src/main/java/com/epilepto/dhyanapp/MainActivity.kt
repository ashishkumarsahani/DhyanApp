package com.epilepto.dhyanapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.epilepto.dhyanapp.data.repository.WearableDataRepository
import com.epilepto.dhyanapp.data.repository.showNotification
import com.epilepto.dhyanapp.navigation.SetupNavGraph
import com.epilepto.dhyanapp.presentation.screens.auth.AuthenticationViewModel
import com.epilepto.dhyanapp.theme.DhayanTheme
import com.epilepto.dhyanapp.utils.DataStoreUtils
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.data.WearDataLayerRegistry
import com.google.android.horologist.data.WearableApiAvailability
import com.google.android.horologist.datalayer.phone.PhoneDataLayerAppHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
@OptIn(ExperimentalHorologistApi::class)
class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    @Inject
    lateinit var dataStoreUtils: DataStoreUtils
    private lateinit var phoneDataLayerAppHelper: PhoneDataLayerAppHelper

    private val wearableDataRepository: WearableDataRepository by lazy {
        WearableDataRepository(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DhayanTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                    //color
                ) {
                    val viewModel: AuthenticationViewModel = hiltViewModel()
                    val dest = viewModel.startDestination.collectAsStateWithLifecycle().value
                    var isAvailable by remember { mutableStateOf(false) }
                    val registry = WearDataLayerRegistry.fromContext(
                        application = this@MainActivity.applicationContext,
                        coroutineScope = lifecycleScope,
                    )

                    LaunchedEffect(key1 = Unit) {
                        isAvailable = WearableApiAvailability.isAvailable(registry.messageClient)
                        if(isAvailable){
                            printNodes()
                        }
                    }
                    phoneDataLayerAppHelper = PhoneDataLayerAppHelper(
                        context = this,
                        registry = registry
                    )

//                    phoneDataLayerAppHelper = PhoneDataLayerAppHelper(
//                        context = this,
//                        registry = registry,
//                    )


                    val navController = rememberNavController()
                    SetupNavGraph(
                        startDestination = dest,
                        phoneDataLayerAppHelper = phoneDataLayerAppHelper,
                        navController = navController,
                    )
                }
            }
        }
    }

    suspend fun printNodes(message: String = "Testing") {
        try {
            val nodes = Wearable.getNodeClient(this).connectedNodes.await()
            withContext(Dispatchers.Main) {
                nodes.forEach { node ->
                    wearableDataRepository.sendMessage(node.id, message,"/UserId").await()
                    Toast.makeText(this@MainActivity, node.displayName, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        wearableDataRepository.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        wearableDataRepository.removeListener(this)
    }

    override fun onMessageReceived(event: MessageEvent) {
        if (event.path == "/session_stated") {
            val data = String(event.data)
            Toast.makeText(
                this,
                data,
                Toast.LENGTH_SHORT
            ).show()
            showNotification(applicationContext, data, "DhyanApp")
        }
    }
}


