package fr.isen.vincent.androidsmartdevice.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.vincent.androidsmartdevice.models.DeviceModel

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    device: DeviceModel,
    isConnected: String,
    onLedToggle: (Int) -> Unit,
    isChecked1: Boolean,
    isChecked3: Boolean,
    onSubscribeToggleButton1: (Boolean) -> Unit,
    onSubscribeToggleButton3: (Boolean) -> Unit,
    counterButton1: Int,
    counterButton3: Int
) {

    var isActivated by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = device.name.ifBlank { "Nom inconnu" },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Adresse MAC :",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = device.macaddress,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Signal :",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${device.signal} dBm",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (isConnected != "✅ Connecté") {
            Text(
                text = "Connexion en cours...",
                style = MaterialTheme.typography.bodyLarge
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            // LED Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "LED 1",
                    tint = if (isActivated == 1) Color.Blue else Color.Gray,
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            if (isActivated != 1) {
                                isActivated = 1
                            } else {
                                isActivated = 0
                            }
                            onLedToggle(1)
                        }
                )
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "LED 2",
                    tint = if (isActivated == 2) Color.Green else Color.Gray,
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            if (isActivated != 2) {
                                isActivated = 2
                            } else {
                                isActivated = 0
                            }
                            onLedToggle(2)
                        }
                )
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "LED 3",
                    tint = if (isActivated == 3) Color.Red else Color.Gray,
                    modifier = Modifier
                        .size(70.dp)
                        .clickable {
                            if (isActivated != 3) {
                                isActivated = 3
                            } else {
                                isActivated = 0
                            }
                            onLedToggle(3)
                        }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscribe to notifications
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked1,
                    onCheckedChange = { onSubscribeToggleButton1(it) }
                )
                Spacer(Modifier.width(20.dp))
                Text(text = "Activer notifications bouton 1")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked3,
                    onCheckedChange = { onSubscribeToggleButton3(it) }
                )
                Spacer(Modifier.width(20.dp))
                Text(text = "Activer notifications bouton 3")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display counters
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Compteur bouton 1
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Compteur bouton 1 :",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$counterButton1",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Compteur bouton 3
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Compteur bouton 3 :",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$counterButton3",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
