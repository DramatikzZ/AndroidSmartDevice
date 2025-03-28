package fr.isen.vincent.androidsmartdevice.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.vincent.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import fr.isen.vincent.androidsmartdevice.screens.ScanScreen
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import fr.isen.vincent.androidsmartdevice.components.TopBar
import fr.isen.vincent.androidsmartdevice.models.DeviceModel
import fr.isen.vincent.androidsmartdevice.utils.AppUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScanActivity : ComponentActivity() {

    private val context = this

    private var isLoading by mutableStateOf(false)

    private val bluetoothLeScanner by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bluetoothLeScanner
    }

    private val devices = mutableStateListOf<DeviceModel>()

    private var scanTimeoutJob: Job? = null


    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val deviceName = result.device.name
            val address = result.device.address
            val rssi = result.rssi

            Log.d("BLE_SCAN", "Résultat: $deviceName | $address | RSSI: $rssi")

            if (!deviceName.isNullOrBlank()) {
                val newDevice = DeviceModel(
                    signal = rssi,
                    name = deviceName,
                    macaddress = address
                )
                if (devices.none { it.macaddress == newDevice.macaddress }) {
                    Log.d("BLE_SCAN", "Ajout à la liste: $deviceName")
                    devices.add(newDevice)
                } else {
                    Log.d("BLE_SCAN", "Déjà dans la liste: $deviceName")
                }
            } else {
                Log.d("BLE_SCAN", "Nom d'appareil null ou vide")
            }
        }


        override fun onScanFailed(errorCode: Int) {
            Log.e("BLE_SCAN", "⚠️ Scan échoué avec code: $errorCode")
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (perm, granted) ->
            Log.d("BLE_PERMS", "$perm accordée ? $granted")
        }

        val allGranted = permissions.all { it.value }

        if (!allGranted) {
            AppUtil.showToast(context, "Certaines permissions sont refusées. Le scan BLE risque de ne pas fonctionner.")
        }
    }

    private fun checkAndRequestBluetoothPermissions() {
        val permissions = mutableListOf<String>()

        // Permissions de base (location est nécessaire pour Bluetooth)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)

        // Vérification des permissions spécifiques selon la version d'Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            permissions.addAll(
                listOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10 & 11
            permissions.addAll(
                listOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            )
        } else { // Android < 10
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
        }

        // Vérification et demande des permissions
        if (permissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            Log.d("BLE_PERMS", "Toutes les permissions déjà accordées")
        }
    }


    @SuppressLint("MissingPermission")
    private fun toggleScan() {
        if (isLoading) {
            Log.d("BLE_SCAN", "Arrêt manuel du scan")
            bluetoothLeScanner.stopScan(scanCallback)
            scanTimeoutJob?.cancel()
        } else {
            Log.d("BLE_SCAN", "Démarrage du scan BLE")
            devices.clear()
            bluetoothLeScanner.startScan(scanCallback)

            // Lancer le timeout automatique (10 secondes)
            scanTimeoutJob = lifecycleScope.launch {
                delay(10_000) // 10 secondes
                Log.d("BLE_SCAN", "Timeout atteint : arrêt du scan automatique")
                bluetoothLeScanner.stopScan(scanCallback)
                isLoading = false
            }
        }

        isLoading = !isLoading
    }


    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        bluetoothLeScanner.stopScan(scanCallback)
        scanTimeoutJob?.cancel()
        isLoading = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestBluetoothPermissions()

        enableEdgeToEdge()

        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() }
                ) { innerPadding ->
                    ScanScreen(
                        modifier = Modifier.padding(innerPadding),
                        devices = devices,
                        isLoading = isLoading,
                        onScanToggle = { toggleScan() }
                    )
                }
            }
        }
    }
}
