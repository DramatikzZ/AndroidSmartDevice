package fr.isen.vincent.androidsmartdevice.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import fr.isen.vincent.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import fr.isen.vincent.androidsmartdevice.screens.DeviceScreen
import fr.isen.vincent.androidsmartdevice.components.TopBar
import fr.isen.vincent.androidsmartdevice.models.DeviceModel
import fr.isen.vincent.androidsmartdevice.viewmodel.DeviceViewModel

class DeviceActivity : ComponentActivity() {

    private lateinit var viewModel: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val device = DeviceModel().apply {
            name = intent.getStringExtra("device_name").toString()
            macaddress = intent.getStringExtra("device_mac_address").toString()
            signal = intent.getIntExtra("device_signal", 0)
        }

        Log.d("DeviceActivity", device.name)
        Log.d("DeviceActivity", device.macaddress)
        Log.d("DeviceActivity", device.signal.toString())

        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() }
                ) { innerPadding ->
                    DeviceScreen(
                        Modifier.padding(innerPadding),
                        device = device,
                        isConnected = viewModel.connectionState.value,
                        onLedToggle = { ledId -> viewModel.toggleLed(ledId) },
                        isChecked1 = viewModel.isChecked1.value,
                        isChecked3 = viewModel.isChecked3.value,
                        onSubscribeToggleButton1 = { enable -> viewModel.toggleNotificationsFor(viewModel.notifCharButton1, enable) },
                        onSubscribeToggleButton3 = { enable -> viewModel.toggleNotificationsFor(viewModel.notifCharButton3, enable) },
                        counterButton1 = viewModel.counterButton1.intValue,
                        counterButton3 = viewModel.counterButton3.intValue
                    )
                }
            }
        }

        viewModel.connectToDevice(this, device.macaddress)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}
