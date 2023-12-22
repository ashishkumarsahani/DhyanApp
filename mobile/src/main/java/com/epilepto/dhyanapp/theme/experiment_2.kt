//package com.epilepto.dhyanapp.theme
//
//import WearDataLayerRegistry
//import android.app.ActivityManager
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Process
//import android.util.Log
//import androidx.annotation.CheckResult
//import com.google.android.gms.common.GoogleApiAvailability
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.AvailabilityException
//import com.google.android.gms.common.api.CommonStatusCodes
//import com.google.android.gms.common.api.GoogleApi
//import com.google.android.gms.wearable.CapabilityClient
//import com.google.android.gms.wearable.Node
//import com.google.android.horologist.annotations.ExperimentalHorologistApi
//import kotlinx.coroutines.TimeoutCancellationException
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withTimeout
//
//object WearableApiAvailability {
//    suspend fun isAvailable(api: GoogleApi<*>): Boolean {
//        return try {
//            GoogleApiAvailability.getInstance()
//                .checkApiAvailability(api)
//                .await()
//
//            true
//        } catch (e: AvailabilityException) {
//            Log.d(
//                TAG,
//                "${api.javaClass.simpleName} API is not available in this device.",
//            )
//            false
//        }
//    }
//
//    val TAG = "WearableApiAvailability"
//}
//
//
//
//
//
////For weardata layer regiastry
//@ExperimentalHorologistApi
//public class PhoneDataLayerAppHelper(
//    context: Context,
//    registry: WearDataLayerRegistry,
//) : DataLayerAppHelper(context, registry) {
//    private val SAMSUNG_COMPANION_PKG = "com.samsung.android.app.watchmanager"
//
//    override suspend fun installOnNode(node: String) {
//        checkIsForegroundOrThrow()
//        val intent = Intent(Intent.ACTION_VIEW)
//            .addCategory(Intent.CATEGORY_BROWSABLE)
//            .setData(Uri.parse(playStoreUri))
//        remoteActivityHelper.startRemoteActivity(intent, node).await()
//    }
//
//    @CheckResult
//    override suspend fun startCompanion(node: String): AppHelperResultCode {
//        checkIsForegroundOrThrow()
//        val companionPackage = registry.nodeClient.getCompanionPackageForNode(node).await()
//
//        /**
//         * Some devices report the wrong companion for actually launching the Companion app: For
//         * example, Samsung devices report the plugin packages that handle comms with GW4, GW5
//         * etc, whereas the package name for the companion *app* is different.
//         */
//        val launchPackage = rewriteCompanionPackageName(companionPackage)
//
//        val intent = context.packageManager.getLaunchIntentForPackage(launchPackage)
//            ?: return AppHelperResultCode.APP_HELPER_RESULT_NO_COMPANION_FOUND
//        try {
//            context.startActivity(intent)
//        } catch (e: ActivityNotFoundException) {
//            return AppHelperResultCode.APP_HELPER_RESULT_ACTIVITY_NOT_FOUND
//        }
//        return AppHelperResultCode.APP_HELPER_RESULT_SUCCESS
//    }
//
//    /**
//     * Some devices report back a different packageName from getCompanionPackageForNode() than is
//     * the actual package of the Companion app. Where this is the case, this lookup ensures the
//     * correct companion app can be launched..
//     */
//    private fun rewriteCompanionPackageName(companionPackage: String): String {
//        val regex = Regex("""com.samsung.*plugin""")
//        return if (regex.matches(companionPackage)) {
//            SAMSUNG_COMPANION_PKG
//        } else {
//            companionPackage
//        }
//    }
//}
//
//internal const val TAG = "DataLayerAppHelper"
//
///**
// * Base class on which of the Wear and Phone DataLayerAppHelpers are build.
// *
// * Provides utility functions for determining installation status and means to install and launch
// * apps as part of the user's journey.
// */
//abstract class DataLayerAppHelper @OptIn(ExperimentalHorologistApi::class) constructor(
//    protected val context: Context,
//    protected val registry: WearDataLayerRegistry,
//) {
//    private val installedDeviceCapabilityUri: String = "wear://*/$CAPABILITY_DEVICE_PREFIX"
//
//    private val activityManager: ActivityManager by lazy { context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
//
//    protected val playStoreUri: String = "market://details?id=${context.packageName}"
//    protected val remoteActivityHelper: RemoteActivityHelper by lazy { RemoteActivityHelper(context) }
//
//    /**
//     * Provides a list of connected nodes and the installation status of the app on these nodes.
//     */
//    public suspend fun connectedNodes(): List<AppHelperNodeStatus> {
//        val connectedNodes = registry.nodeClient.connectedNodes.await()
//        val nearbyNodes = connectedNodes.filter { it.isNearby }
//        val capabilities =
//            registry.capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE).await()
//
//        val installedPhoneNodes = capabilities[PHONE_CAPABILITY]?.nodes?.map { it.id } ?: setOf()
//        val installedWatchNodes = capabilities[WATCH_CAPABILITY]?.nodes?.map { it.id } ?: setOf()
//        val allInstalledNodes = installedPhoneNodes + installedWatchNodes
//
//        return nearbyNodes.map {
//            AppHelperNodeStatus(
//                id = it.id,
//                displayName = it.displayName,
//                isAppInstalled = allInstalledNodes.contains(it.id),
//                surfacesInfo = getSurfaceStatus(it.id),
//                nodeType = when (it.id) {
//                    in installedPhoneNodes -> AppHelperNodeType.PHONE
//                    in installedWatchNodes -> AppHelperNodeType.WATCH
//                    else -> AppHelperNodeType.UNKNOWN
//                },
//            )
//        }
//    }
//
//    private suspend fun getSurfaceStatus(nodeId: String) = registry.protoFlow(
//        targetNodeId = TargetNodeId.SpecificNodeId(nodeId),
//        serializer = SurfaceInfoSerializer,
//        path = SURFACE_INFO_PATH,
//    ).first()
//
//    /**
//     * Creates a flow to keep the client updated with the set of connected devices with the app
//     * installed.
//     */
//    public val connectedAndInstalledNodes = callbackFlow<Set<Node>> {
//        val listener: CapabilityClient.OnCapabilityChangedListener =
//            CapabilityClient.OnCapabilityChangedListener { capability ->
//                @Suppress("UNUSED_VARIABLE")
//                val unused =
//                    trySend(capability.nodes.filter { it.isNearby }.toSet())
//            }
//
//        val allCaps = registry.capabilityClient.getAllCapabilities(
//            CapabilityClient.FILTER_REACHABLE,
//        ).await()
//        val installedCaps =
//            allCaps.filter { it.key.startsWith(CAPABILITY_DEVICE_PREFIX) }.values.flatMap { it.nodes }
//                .filter { it.isNearby }.toSet()
//
//        @Suppress("UNUSED_VARIABLE")
//        val unused = trySend(installedCaps)
//        registry.capabilityClient.addListener(
//            listener,
//            Uri.parse(installedDeviceCapabilityUri),
//            CapabilityClient.FILTER_PREFIX,
//        )
//        awaitClose {
//            registry.capabilityClient.removeListener(listener)
//        }
//    }
//
//    /**
//     * Launches to the appropriate store on the specified node to allow installation of the app.
//     *
//     * @param node The node to launch on.
//     */
//
//    abstract suspend fun installOnNode(node: String)
//
//    /**
//     * Starts the companion that relates to the specified node. This will start on the phone,
//     * irrespective of whether the specified node is a phone or a watch.
//     *
//     * @param node The node to launch on.
//     * @return Whether launch was successful or not.
//     */
//    @CheckResult
//    abstract suspend fun startCompanion(node: String): AppHelperResultCode
//
//    /**
//     * Launch an activity on the specified node.
//     */
//    @CheckResult
//    public suspend fun startRemoteActivity(
//        node: String,
//        config: ActivityConfig,
//    ): AppHelperResultCode {
//        checkIsForegroundOrThrow()
//        val request = launchRequest { activity = config }
//        return sendRequestWithTimeout(node, LAUNCH_APP, request.toByteArray())
//    }
//
//    /**
//     * Launch own app on the specified node.
//     */
//    @CheckResult
//    public suspend fun startRemoteOwnApp(node: String): AppHelperResultCode {
//        checkIsForegroundOrThrow()
//        val request = launchRequest { ownApp = ownAppConfig { } }
//        return sendRequestWithTimeout(node, LAUNCH_APP, request.toByteArray())
//    }
//
//    /**
//     * Attempts a [MessageClient#sendRequest] with a timeout, covering both the GMS Timeout that may
//     * occur (after 1 minute?) but also a timeout specified by Horologist, as 1 minute is often too
//     * long.
//     */
//    @CheckResult
//    protected suspend fun sendRequestWithTimeout(
//        node: String,
//        path: String,
//        data: ByteArray,
//        timeoutMs: Long = MESSAGE_REQUEST_TIMEOUT_MS,
//    ): AppHelperResultCode {
//        checkIsForegroundOrThrow()
//        val response = try {
//            withTimeout(timeoutMs) {
//                // Cancellation will not lead to the GMS Task itself being cancelled.
//                registry.messageClient.sendRequest(node, path, data).await()
//            }
//        } catch (timeoutException: TimeoutCancellationException) {
//            return AppHelperResultCode.APP_HELPER_RESULT_TIMEOUT
//        } catch (e: ApiException) {
//            if (e.statusCode == CommonStatusCodes.TIMEOUT) {
//                return AppHelperResultCode.APP_HELPER_RESULT_TIMEOUT
//            } else {
//                throw e
//            }
//        }
//        return AppHelperResult.parseFrom(response).code
//    }
//
//    /**
//     * Provides a check that the current process is running in the foreground. This is used in all
//     * methods within AppHelper that start some form of Activity on the other device, to ensure that
//     * apps are not being launched as a result of a background process on the calling device.
//     */
//    protected fun checkIsForegroundOrThrow() {
//        val isForeground = activityManager.runningAppProcesses.find {
//            it.pid == Process.myPid()
//        }?.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//        if (!isForeground) {
//            throw SecurityException("This method can only be called from the foreground.")
//        }
//    }
//
//    /**
//     * Check whether the data layer is available before use to avoid crashes.
//     */
//    public suspend fun isAvailable(): Boolean =
//        // Check CapabilityClient as a proxy for all APIs being available
//        WearableApiAvailability.isAvailable(registry.capabilityClient)
//
//    public companion object {
//        public const val DATA_LAYER_APP_HELPER: String = "data_layer_app_helper"
//        public const val CAPABILITY_DEVICE_PREFIX = "${DATA_LAYER_APP_HELPER}_device"
//        public const val PHONE_CAPABILITY = "${CAPABILITY_DEVICE_PREFIX}_phone"
//        public const val WATCH_CAPABILITY = "${CAPABILITY_DEVICE_PREFIX}_watch"
//        public const val LAUNCH_APP: String = "/$DATA_LAYER_APP_HELPER/launch_app"
//        public const val SURFACE_INFO_PATH: String = "/$DATA_LAYER_APP_HELPER/surface_info"
//        public const val MESSAGE_REQUEST_TIMEOUT_MS = 15_000L
//    }
//}
//
//fun byteArrayForResultCode(resultCode: AppHelperResultCode): ByteArray {
//    val response = appHelperResult {
//        code = resultCode
//    }
//    return response.toByteArray()
//}
