package com.epilepto.dhyanapp.presentation.screens.main_screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.wearable.Node

@Composable
fun HomeScreen(
    node: Node?,
    onConnectToWatch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Paired Watch",
            style = MaterialTheme.typography.titleLarge
        )

        if (node != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(ShapeDefaults.Medium.topStart)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = node.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "ID: ${node.id}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(onClick = onConnectToWatch) {
                        Text(text = "Connect")
                    }
                }
            }
        } else {
            Text(text = "No Nodes Nearby", style = MaterialTheme.typography.titleMedium)
        }

    }
}