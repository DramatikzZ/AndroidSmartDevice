package fr.isen.vincent.androidsmartdevice.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {
    private var bluetoothGatt: BluetoothGatt? = null
    private val isLedOn = mutableStateMapOf<Int, Boolean>()
    val isChecked1 = mutableStateOf(false)
    val isChecked3 = mutableStateOf(false)
    val counterButton1 = mutableIntStateOf(0)
    val counterButton3 = mutableIntStateOf(0)
    val connectionState = mutableStateOf("Appuyez sur le bouton pour vous connecter")

    var notifCharButton1: BluetoothGattCharacteristic? = null
    var notifCharButton3: BluetoothGattCharacteristic? = null

    @SuppressLint("MissingPermission")
    fun connectToDevice(context: Context, address: String) {
        connectionState.value = "Connexion BLE en cours..."
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val device = bluetoothAdapter.getRemoteDevice(address)

        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    connectionState.value = "✅ Connecté"
                    gatt.discoverServices()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectionState.value = "❌ Déconnecté"
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                val service3 = gatt.services.getOrNull(2)
                val service4 = gatt.services.getOrNull(3)

                notifCharButton3 = service3?.characteristics?.getOrNull(1)
                notifCharButton1 = service4?.characteristics?.getOrNull(0)

                Log.d("BLE", "Notif bouton 1 = $notifCharButton1")
                Log.d("BLE", "Notif bouton 3 = $notifCharButton3")
            }

            @Deprecated("Deprecated in Java")
            override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
                when (characteristic) {
                    notifCharButton1 -> {
                        val value = characteristic.value.firstOrNull()?.toInt() ?: return
                        counterButton1.intValue = value
                        Log.d("BLE", "📥 Bouton 1 → compteur = $value")
                    }

                    notifCharButton3 -> {
                        val value = characteristic.value.firstOrNull()?.toInt() ?: return
                        counterButton3.intValue = value
                        Log.d("BLE", "📥 Bouton 3 → compteur = $value")
                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun toggleNotificationsFor(characteristic: BluetoothGattCharacteristic?, enable: Boolean) {
        if (characteristic == null) return

        bluetoothGatt?.setCharacteristicNotification(characteristic, enable)

        val descriptor = characteristic.getDescriptor(
            characteristic.descriptors.firstOrNull()?.uuid ?: return
        )
        descriptor.value = if (enable)
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        else
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE

        bluetoothGatt?.writeDescriptor(descriptor)

        when (characteristic) {
            notifCharButton1 -> {
                isChecked1.value = enable
            }
            notifCharButton3 -> {
                isChecked3.value = enable
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun toggleLed(ledId: Int) {
        bluetoothGatt?.let { gatt ->
            val characteristic = gatt.services[2].characteristics[0]
            val currentlyOn = isLedOn[ledId] ?: false
            val command = if (currentlyOn) {
                0x00
            } else {
                when (ledId) {
                    1 -> 0x01
                    2 -> 0x02
                    3 -> 0x03
                    else -> 0x00
                }
            }
            isLedOn[ledId] = !currentlyOn
            characteristic.value = byteArrayOf(command.toByte())
            gatt.writeCharacteristic(characteristic)
            if (!currentlyOn) {
                isLedOn.keys.forEach { key ->
                    if (key != ledId) isLedOn[key] = false
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public override fun onCleared() {
        super.onCleared()
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}
