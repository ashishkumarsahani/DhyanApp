//import WearDataLayerRegistry.Companion.buildUri
//import android.annotation.SuppressLint
//import android.content.Context
//import android.net.Uri
//import android.util.Log
//import androidx.datastore.core.CorruptionException
//import androidx.datastore.core.DataStore
//import androidx.datastore.core.Serializer
//import androidx.datastore.preferences.PreferencesProto
//import androidx.datastore.preferences.core.MutablePreferences
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.booleanPreferencesKey
//import androidx.datastore.preferences.core.doublePreferencesKey
//import androidx.datastore.preferences.core.emptyPreferences
//import androidx.datastore.preferences.core.floatPreferencesKey
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.core.longPreferencesKey
//import androidx.datastore.preferences.core.mutablePreferencesOf
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.core.stringSetPreferencesKey
//import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
//import com.google.android.gms.wearable.CapabilityClient
//import com.google.android.gms.wearable.DataClient
//import com.google.android.gms.wearable.DataEvent
//import com.google.android.gms.wearable.DataItem
//import com.google.android.gms.wearable.MessageClient
//import com.google.android.gms.wearable.NodeClient
//import com.google.android.gms.wearable.PutDataRequest
//import com.google.android.gms.wearable.Wearable
//import com.google.android.horologist.annotations.ExperimentalHorologistApi
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.FlowCollector
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.flow.emitAll
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.shareIn
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.sync.Mutex
//import kotlinx.coroutines.sync.withLock
//import kotlinx.coroutines.tasks.await
//import java.io.ByteArrayInputStream
//import java.io.ByteArrayOutputStream
//import java.io.IOException
//import java.io.InputStream
//import java.io.OutputStream
//import kotlin.reflect.KClass
//
//@SuppressLint("VisibleForTests")
//@ExperimentalHorologistApi
///**
// * Implementation of Androidx Datastore for Proto and Preferences on top of the
// * Wearable DataClient.
// *
// * See https://developer.android.com/topic/libraries/architecture/datastore for DataStore features.
// */
//public class WearDataLayerRegistry(
//    public val dataClient: DataClient,
//    public val nodeClient: NodeClient,
//    public val messageClient: MessageClient,
//    public val capabilityClient: CapabilityClient,
//    private val coroutineScope: CoroutineScope,
//) {
//    public val serializers: SerializerRegistry = SerializerRegistry()
//    private val protoDataListeners: MutableList<ProtoDataListenerRegistration<*>> = mutableListOf()
//
//    init {
//        serializers.registerSerializer(PreferencesSerializer)
//    }
//
//    /**
//     * Returns a local Proto DataStore for the given Proto structure.
//     *
//     * @param path the path inside the data client namespace, of the form "/xyz/123"
//     * @param coroutineScope the coroutine scope used for the internal SharedFlow.
//     * @param serializer the data store Serializer
//     * @param started the SharingStarted mode for the internal SharedFlow.
//     */
//    fun <T> protoDataStore(
//        path: String,
//        coroutineScope: CoroutineScope,
//        serializer: Serializer<T>,
//        started: SharingStarted = SharingStarted.WhileSubscribed(5000),
//    ): DataStore<T> {
//        return WearLocalDataStore(
//            this,
//            started = started,
//            coroutineScope = coroutineScope,
//            serializer = serializer,
//            path = path,
//        )
//    }
//
//    fun <T> protoFlow(
//        targetNodeId: TargetNodeId,
//        serializer: Serializer<T>,
//        path: String,
//    ): Flow<T> = flow {
//        val nodeId = targetNodeId.evaluate(this@WearDataLayerRegistry)
//
//        if (nodeId != null) {
//            emitAll(dataClient.dataItemFlow(nodeId, path, serializer))
//        }
//    }
//
//    inline fun <reified T : Any> registerSerializer(serializer: Serializer<T>) {
//        serializers.registerSerializer(serializer)
//    }
//
//    inline fun <reified T : Any> registerProtoDataListener(
//        path: String,
//        listener: ProtoDataListener<T>,
//    ) {
//        val serializer = serializers.serializerForType<T>()
//        registerProtoDataListener(path, listener, serializer)
//    }
//
//    fun <T : Any> registerProtoDataListener(
//        path: String,
//        listener: ProtoDataListener<T>,
//        serializer: Serializer<T>,
//    ) {
//        val element = ProtoDataListenerRegistration(path, serializer, listener)
//        protoDataListeners.add(element)
//    }
//
//    fun onDataChanged(dataEvents: List<DataEvent>) {
//        runBlocking {
//            for (dataEvent in dataEvents) {
//                val listeners = protoDataListeners.filter { it.path == dataEvent.dataItem.uri.path }
//
//                if (listeners.isNotEmpty()) {
//                    fireListeners(dataEvent, listeners)
//                }
//            }
//        }
//    }
//
//    private suspend fun fireListeners(
//        dataEvent: DataEvent,
//        listeners: List<ProtoDataListenerRegistration<*>>,
//    ) {
//        val nodeId = dataEvent.dataItem.uri.host!!
//        val path = dataEvent.dataItem.uri.path!!
//        if (dataEvent.type == DataEvent.TYPE_CHANGED) {
//            val data = dataEvent.dataItem.data!!
//            for (listener in listeners) {
//                listener.dataAdded(nodeId, path, data)
//            }
//        } else {
//            for (listener in listeners) {
//                listener.dataDeleted(nodeId, path)
//            }
//        }
//    }
//
//    companion object {
//        /**
//         * Create an instance looking up Wearable DataClient and NodeClient using the given context.
//         */
//        fun fromContext(
//            application: Context,
//            coroutineScope: CoroutineScope,
//        ): WearDataLayerRegistry = WearDataLayerRegistry(
//            dataClient = Wearable.getDataClient(application),
//            nodeClient = Wearable.getNodeClient(application),
//            messageClient = Wearable.getMessageClient(application),
//            capabilityClient = Wearable.getCapabilityClient(application),
//            coroutineScope = coroutineScope,
//        )
//
//        public fun buildUri(nodeId: String, path: String): Uri = Uri.Builder()
//            .scheme(PutDataRequest.WEAR_URI_SCHEME)
//            .authority(nodeId)
//            .path(path)
//            .build()
//
//        public fun dataStorePath(t: KClass<*>, persisted: Boolean = false): String {
//            // Use predictable paths for persisted data that should survive backups
//            val prefix = if (persisted) "proto/persisted" else "proto"
//            return "/$prefix/${t.simpleName!!}"
//        }
//
//        fun preferencesPath(name: String, persisted: Boolean = false): String {
//            // Use predictable paths for persisted data that should survive backups
//            val prefix = if (persisted) "proto/persisted" else "proto"
//            return "/$prefix/prefs/$name"
//        }
//    }
//}
//
//class SerializerRegistry {
//    private val serializers: MutableMap<KClass<*>, Serializer<*>> = mutableMapOf()
//
//    public inline fun <reified T : Any> serializerForType() = serializerForType(T::class)
//
//    @Suppress("UNCHECKED_CAST")
//    public fun <T : Any> serializerForType(type: KClass<T>): Serializer<T> =
//        serializers[type] as Serializer<T>?
//            ?: throw IllegalStateException("Serializer for $type not registered")
//
//    public inline fun <reified T : Any> registerSerializer(serializer: Serializer<T>) {
//        registerSerializer(T::class, serializer)
//    }
//
//    public fun <T : Any> registerSerializer(type: KClass<T>, serializer: Serializer<T>) {
//        serializers[type] = serializer
//    }
//}
//
//class ProtoDataListenerRegistration<T>(
//    val path: String,
//    val serializer: Serializer<T>,
//    val listener: ProtoDataListener<T>,
//) {
//    suspend fun dataAdded(nodeId: String, path: String, data: ByteArray) {
//        val item = serializer.readFrom(ByteArrayInputStream(data))
//        listener.dataAdded(nodeId, path, item)
//    }
//
//    suspend fun dataDeleted(nodeId: String, path: String) {
//        listener.dataDeleted(nodeId, path)
//    }
//}
//
//interface ProtoDataListener<T> {
//    fun dataAdded(nodeId: String, path: String, value: T)
//
//    fun dataDeleted(nodeId: String, path: String)
//}
//
//internal object PreferencesSerializer : Serializer<Preferences> {
//    override val defaultValue: Preferences
//        get() {
//            return emptyPreferences()
//        }
//
//    @Throws(IOException::class, CorruptionException::class)
//    override suspend fun readFrom(input: InputStream): Preferences {
//        val preferencesProto = try {
//            PreferencesProto.PreferenceMap.parseFrom(input)
//        } catch (ipbe: InvalidProtocolBufferException) {
//            throw CorruptionException("Unable to parse preferences proto.", ipbe)
//        }
//
//        val mutablePreferences = mutablePreferencesOf()
//
//        for ((name, value) in preferencesProto.preferencesMap) {
//            addProtoEntryToPreferences(name, value, mutablePreferences)
//        }
//
//        return mutablePreferences.toPreferences()
//    }
//
//    @Throws(IOException::class, CorruptionException::class)
//    override suspend fun writeTo(t: Preferences, output: OutputStream) {
//        val preferences = t.asMap()
//        val protoBuilder = PreferencesProto.PreferenceMap.newBuilder()
//
//        for ((key, value) in preferences) {
//            protoBuilder.putPreferences(key.name, getValueProto(value))
//        }
//
//        protoBuilder.build().writeTo(output)
//    }
//
//    private fun getValueProto(value: Any): PreferencesProto.Value {
//        @Suppress("UNCHECKED_CAST")
//        return when (value) {
//            is Boolean -> PreferencesProto.Value.newBuilder().setBoolean(value).build()
//            is Float -> PreferencesProto.Value.newBuilder().setFloat(value).build()
//            is Double -> PreferencesProto.Value.newBuilder().setDouble(value).build()
//            is Int -> PreferencesProto.Value.newBuilder().setInteger(value).build()
//            is Long -> PreferencesProto.Value.newBuilder().setLong(value).build()
//            is String -> PreferencesProto.Value.newBuilder().setString(value).build()
//            is Set<*> -> PreferencesProto.Value.newBuilder().setStringSet(
//                PreferencesProto.StringSet.newBuilder().addAllStrings(value as Set<String>),
//            ).build()
//
//            else -> throw IllegalStateException(
//                "PreferencesSerializer does not support type: ${value.javaClass.name}",
//            )
//        }
//    }
//
//    private fun addProtoEntryToPreferences(
//        name: String,
//        value: PreferencesProto.Value,
//        mutablePreferences: MutablePreferences,
//    ) {
//        return when (value.valueCase) {
//            PreferencesProto.Value.ValueCase.BOOLEAN ->
//                mutablePreferences[booleanPreferencesKey(name)] =
//                    value.boolean
//
//            PreferencesProto.Value.ValueCase.FLOAT -> mutablePreferences[floatPreferencesKey(name)] = value.float
//            PreferencesProto.Value.ValueCase.DOUBLE -> mutablePreferences[doublePreferencesKey(name)] = value.double
//            PreferencesProto.Value.ValueCase.INTEGER -> mutablePreferences[intPreferencesKey(name)] = value.integer
//            PreferencesProto.Value.ValueCase.LONG -> mutablePreferences[longPreferencesKey(name)] = value.long
//            PreferencesProto.Value.ValueCase.STRING -> mutablePreferences[stringPreferencesKey(name)] = value.string
//            PreferencesProto.Value.ValueCase.STRING_SET ->
//                mutablePreferences[stringSetPreferencesKey(name)] =
//                    value.stringSet.stringsList.toSet()
//
//            PreferencesProto.Value.ValueCase.VALUE_NOT_SET ->
//                throw CorruptionException("Value not set.")
//
//            null -> throw CorruptionException("Value case is null.")
//            else -> throw CorruptionException("Value case is not supported.")
//        }
//    }
//}
//
//@SuppressLint("VisibleForTests")
//@ExperimentalHorologistApi
//public class WearLocalDataStore<T>(
//    private val wearDataLayerRegistry: WearDataLayerRegistry,
//    started: SharingStarted = SharingStarted.Eagerly,
//    coroutineScope: CoroutineScope,
//    private val serializer: Serializer<T>,
//    private val path: String,
//) : DataStore<T> {
//    private val nodeIdFlow = nodeIdFlow().shareIn(coroutineScope, started = started, replay = 1)
//
//    private val mutex = Mutex()
//
//    val dataClient: DataClient
//        get() = wearDataLayerRegistry.dataClient
//
//    private fun nodeIdFlow() = flow {
//        val nodeId = TargetNodeId.ThisNodeId.evaluate(wearDataLayerRegistry)
//        emit(
//            NodeIdAndPath(
//                nodeId = nodeId,
//                fullPath = buildUri(nodeId, path),
//            ),
//        )
//    }
//
//    private val sharedFlow: SharedFlow<T> = nodeIdFlow.flatMapLatest { (nodeId, _) ->
//        dataClient.dataItemFlow(
//            nodeId,
//            path,
//            serializer,
//        )
//    }.shareIn(coroutineScope, started = SharingStarted.Eagerly, replay = 1)
//
//    override val data: Flow<T> = sharedFlow
//
//    private suspend fun writeBytes(t: T): ByteArray {
//        return ByteArrayOutputStream().apply {
//            serializer.writeTo(t, this)
//        }.toByteArray()
//    }
//
//    override suspend fun updateData(transform: suspend (t: T) -> T): T = mutex.withLock {
//        val nodeId = nodeIdFlow.first()
//
//        val oldT = readExistingValue(nodeId)
//        val newT = transform(oldT)
//
//        if (newT == null) {
//            dataClient.deleteDataItems(nodeId.fullPath)
//                .await()
//        } else if (newT != oldT) {
//            val request = PutDataRequest.create(path).apply {
//                data = writeBytes(newT)
//            }
//
//            dataClient.putDataItem(request).await()
//        }
//
//        return newT
//    }
//
//    private suspend fun readExistingValue(nodeId: NodeIdAndPath): T {
//        try {
//            val data = dataClient.getDataItem(buildUri(nodeId.nodeId, path)).await().data
//
//            if (data != null) {
//                return serializer.readFrom(ByteArrayInputStream(data))
//            }
//        } catch (e: Exception) {
//            // TODO introduce CorruptionHandler
//            Log.w("WearLocalDataStore", "Corrupted data for DataStore")
//        }
//
//        return serializer.defaultValue
//    }
//}
//
//data class NodeIdAndPath(
//    val nodeId: String,
//    val fullPath: Uri,
//)
//
//@SuppressLint("VisibleForTests")
//public fun <T> DataClient.dataItemFlow(
//    nodeId: String,
//    path: String,
//    serializer: Serializer<T>,
//    defaultValue: () -> T = { serializer.defaultValue },
//): Flow<T> = callbackFlow {
//    val listener = DataClient.OnDataChangedListener {
//        @SuppressWarnings("GmsCoreFirstPartyApiChecker")
//        val dataItem = it[it.count - 1].dataItem
//        val data = dataItem.data
//
//        @Suppress("UNUSED_VARIABLE")
//        val unused = trySend(data)
//    }
//
//    val uri = Uri.Builder()
//        .scheme(PutDataRequest.WEAR_URI_SCHEME)
//        .path(path)
//        .authority(nodeId)
//        .build()
//
//    addListener(
//        listener,
//        uri,
//        DataClient.FILTER_LITERAL,
//    ).await() // Ensure we are subscribed to updates first,
//
//    val item: DataItem? = this@dataItemFlow.getDataItem(uri).await() // then get the current value.
//
//    @Suppress("UNUSED_VARIABLE")
//    val unused = trySend(item?.data)
//
//    awaitClose {
//        removeListener(listener)
//    }
//}.map {
//    if (it != null) {
//        serializer.parse(it)
//    } else {
//        defaultValue()
//    }
//}
//
//private suspend fun <T> Serializer<T>.parse(
//    data: ByteArray,
//) = readFrom(ByteArrayInputStream(data))
//
//public interface TargetNodeId {
//    /**
//     * Return the node id for the given strategy.
//     */
//    @OptIn(ExperimentalHorologistApi::class)
//    public suspend fun evaluate(dataLayerRegistry: WearDataLayerRegistry): String?
//
//    /**
//     * A reference to the Node for this device.
//     */
//    public object ThisNodeId : TargetNodeId {
//        @OptIn(ExperimentalHorologistApi::class)
//        override suspend fun evaluate(dataLayerRegistry: WearDataLayerRegistry): String {
//            return dataLayerRegistry.nodeClient.localNode.await().id
//        }
//    }
//
//    /**
//     * A reference to the single paired node for the connected phone.
//     * All wear devices must have a single connected device, although it may not be available.
//     */
//    public object PairedPhone : TargetNodeId {
//        @OptIn(ExperimentalHorologistApi::class)
//        override suspend fun evaluate(dataLayerRegistry: WearDataLayerRegistry): String? {
//            val capabilitySearch = dataLayerRegistry.capabilityClient.getCapability(
//                HOROLOGIST_PHONE,
//                CapabilityClient.FILTER_ALL,
//            ).await()
//
//            return capabilitySearch.nodes.singleOrNull()?.id
//        }
//    }
//
//    /**
//     * A reference to a specific node id, via prior configuration.
//     */
//    public class SpecificNodeId(
//        public val nodeId: String,
//    ) : TargetNodeId {
//        @ExperimentalHorologistApi
//        override suspend fun evaluate(dataLayerRegistry: WearDataLayerRegistry): String {
//            return nodeId
//        }
//    }
//
//    companion object {
//        const val HOROLOGIST_PHONE = "horologist_phone"
//        const val HOROLOGIST_WATCH = "horologist_watch"
//    }
//}
//public suspend fun <T> FlowCollector<T>.emitAll(flow: Flow<T>) {
//    ensureActive()
//    flow.collect(this)
//}
//internal fun FlowCollector<*>.ensureActive() {
//    if (this is ThrowingCollector) throw e
//}
//
//internal class ThrowingCollector(@JvmField val e: Throwable) : FlowCollector<Any?> {
//    override suspend fun emit(value: Any?) {
//        throw e
//    }
//}
//
