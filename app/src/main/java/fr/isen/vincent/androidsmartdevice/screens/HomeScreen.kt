package fr.isen.vincent.androidsmartdevice.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.vincent.androidsmartdevice.R
import fr.isen.vincent.androidsmartdevice.activities.ScanActivity
import fr.isen.vincent.androidsmartdevice.components.TopBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(modifier)

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Bienvenue dans votre application Smart Device"
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Pour démarrer vos interactions avec les appareils BLE environnants cliquer sur commencer"
            )

            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(R.drawable.bluetooth_image),
                contentDescription = "bluetooth image",
                modifier = Modifier.size(width = 300.dp, height = 300.dp)
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    val intent = Intent(context, ScanActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Commencer"
                )
            }
        }
    }
}