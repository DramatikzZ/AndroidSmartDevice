package fr.isen.vincent.androidsmartdevice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.vincent.androidsmartdevice.components.TopBar

@Composable
fun ScanScreen(modifier : Modifier = Modifier) {


    var isLoading by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxSize()) {

        TopBar(modifier)

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    isLoading = !isLoading
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                    Text(
                        text = if(!isLoading) "Lancer la recherche Bluetooth" else "Recherche d'appareils"
                    )

                    Spacer(modifier.width(20.dp))

                    Icon(
                        imageVector = if(!isLoading) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Icone"
                    )

            }
        }
    }
}