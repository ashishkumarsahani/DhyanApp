package com.epilepto.dhyanapp.presentation.screens.pairing//package com.epilepto.dhayan.presentation.screens.pairing
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.epilepto.dhayan.data.bluetooth.core.BluetoothController
//import com.epilepto.dhayan.data.bluetooth.model.BluetoothDeviceDTO
//import com.epilepto.dhayan.data.bluetooth.core.BluetoothDevices
//import com.epilepto.dhayan.data.bluetooth.model.ConnectionResult
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import javax.inject.Inject
//
//@HiltViewModel
//class PairingViewModel @Inject constructor(
//    private val bluetoothController: BluetoothController
//) : ViewModel() {
//    private var deviceConnectionJob: Job? = null
//
//    private val _uiState = MutableStateFlow(BluetoothUiState())
//
//    val uiState = combine(
//        bluetoothController.scannedDevices,
//        bluetoothController.pairedDevices,
//        _uiState
//    ) { scannedDevices, pairedDevices, state ->
//        state.copy(
//            scannedDevices = scannedDevices,
//            pairedDevices = pairedDevices
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.value)
//    init {
//        bluetoothController.isConnected.onEach { isConnected ->
//            _uiState.update { it.copy(isConnected = isConnected) }
//        }.launchIn(viewModelScope)
//
//        bluetoothController.errors.onEach { error ->
//            _uiState.update { it.copy(error = error) }
//        }.launchIn(viewModelScope)
//    }
//
//    fun connectToDevice(device: BluetoothDeviceDTO) {
//        _uiState.update { it.copy(isConnecting = true) }
//        deviceConnectionJob = bluetoothController
//            .connectToDevice(device)
//            .listen()
//    }
//
//    fun disconnectFromDevice() {
//        deviceConnectionJob?.cancel()
//        bluetoothController.closeConnection()
//        _uiState.update {
//            it.copy(
//                isConnecting = false,
//                isConnected = false
//            )
//        }
//    }
//
//    fun waitForIncomingConnections() {
//        _uiState.update { it.copy(isConnecting = true) }
//        deviceConnectionJob = bluetoothController.startBluetoothServer().listen()
//    }
//
//    fun startScan() {
//        bluetoothController.startDiscovery()
//    }
//
//    private fun stopScan() {
//        bluetoothController.stopDiscovery()
//    }
//
//    fun restartScan() {
//        stopScan()
//        startScan()
//    }
//
//    private fun Flow<ConnectionResult>.listen(): Job {
//        return onEach { result ->
//            when (result) {
//                is ConnectionResult.ConnectionEstablished -> {
//                    _uiState.update {
//                        it.copy(
//                            isConnecting = false,
//                            isConnected = true,
//                            error = null
//                        )
//                    }
//                }
//
//                is ConnectionResult.Error -> {
//                    _uiState.update {
//                        it.copy(
//                            isConnected = false,
//                            isConnecting = false,
//                            error = result.error
//                        )
//                    }
//                }
//            }
//        }.catch { throwable ->
//            bluetoothController.closeConnection()
//            _uiState.update {
//                it.copy(
//                    isConnected = false,
//                    isConnecting = false,
//                    error = throwable.message ?: it.error
//                )
//            }
//        }.launchIn(viewModelScope)
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        bluetoothController.release()
//    }
//
//    data class BluetoothUiState(
//        val scannedDevices: BluetoothDevices = emptyList(),
//        val pairedDevices: BluetoothDevices = emptyList(),
//        val isConnected: Boolean = false,
//        val isConnecting: Boolean = false,
//        val error: String? = null
//    )
//}