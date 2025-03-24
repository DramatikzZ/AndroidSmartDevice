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
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class ScanActivity : ComponentActivity() {

    private val REQUEST_CODE_BLUETOOTH_PERMISSIONS = 1

    private fun checkAndRequestBluetoothPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        } else {
            // Android < 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQUEST_CODE_BLUETOOTH_PERMISSIONS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        checkAndRequestBluetoothPermissions()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ScanScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}