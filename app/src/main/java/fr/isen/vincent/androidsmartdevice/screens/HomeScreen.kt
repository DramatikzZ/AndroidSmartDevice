package fr.isen.vincent.androidsmartdevice.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.isen.vincent.androidsmartdevice.activities.ScanActivity
import fr.isen.vincent.androidsmartdevice.components.TopBar
import fr.isen.vincent.androidsmartdevice.utils.AppUtil

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val case by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopBar(modifier)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Column {
                Text(
                    text = "Bienvenue dans votre application Smart Device",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Pour démarrer vos interactions avec les appareils BLE environnants, cliquez sur le bouton ci-dessous.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(20.dp))

            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Bluetooth image",
                modifier = Modifier.size(220.dp)
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when (case) {
                        0 -> {
                            val intent = Intent(context, ScanActivity::class.java)
                            context.startActivity(intent)
                        }
                        1 -> AppUtil.showToast(context, "Bluetooth non dispo")
                        2 -> AppUtil.showToast(context, "Bluetooth non activé")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "COMMENCER"
                )
            }
        }
    }
}
