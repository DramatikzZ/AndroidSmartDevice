package fr.isen.vincent.androidsmartdevice.screens

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vincent.androidsmartdevice.components.TopBar
import fr.isen.vincent.androidsmartdevice.models.DeviceModel
import fr.isen.vincent.androidsmartdevice.utils.AppUtil

@Composable
fun ScanScreen(modifier : Modifier = Modifier) {

    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }

    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopBar(modifier)

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        bluetoothAdapter == null -> AppUtil.showToast(context, "Bluetooth non disponible")
                        !bluetoothAdapter.isEnabled -> AppUtil.showToast(context, "Bluetooth non activé")
                        else -> {
                            isLoading = !isLoading
                            //On lance le scan
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = if (!isLoading) "Lancer le scan BLE" else "Scan BLE en cours...",
                        fontSize = 22.sp
                    )

                    Spacer(modifier.width(20.dp))

                    Icon(
                        imageVector = if (!isLoading) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Icone",
                        Modifier.size(40.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            val devices = remember {
                listOf(
                    DeviceModel(signal = -45, name = "SmartWatch", macaddress = "00:11:22:33:44:55"),
                    DeviceModel(signal = -60, name = "Casque Audio", macaddress = "AA:BB:CC:DD:EE:FF")
                )
            }

            LazyColumn {
                items(devices.size) { index ->
                    if(index!=0){
                        HorizontalDivider(modifier = Modifier.padding(8.dp))
                    }
                    DeviceItem(devices[index])
                }
            }
        }
    }
}

@Composable
fun DeviceItem(device: DeviceModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)),
        ) {
            Text(
                text = "${device.signal}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White ,
                modifier = Modifier.padding(16.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = device.name.ifBlank { "Nom inconnu" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Adresse MAC : ${device.macaddress}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}