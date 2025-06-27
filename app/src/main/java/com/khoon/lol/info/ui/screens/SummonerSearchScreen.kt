package com.khoon.lol.info.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.layout.ContentScale
import com.khoon.lol.info.R
import androidx.compose.material3.MenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummonerSearchScreen() {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val servers = listOf(
        "NA", // North America
        "EUW", // Europe West
        "EUNE", // Europe Nordic & East
        "KR", // Korea
        "JP", // Japan
        "OCE", // Oceania
        "LAN", // Latin America North
        "LAS", // Latin America South
        "TR", // Turkey
        "BR", // Brazil
        "RU", // Russia
        "SEA", // Southeast Asia
        "ME", // Middle East
        "VN", // Vietnam
        "TW", // Taiwan/Hong Kong/Macao
        "SG", // Singapore
        "CN", // China
        "PH", // Philippines
        "TH", // Thailand
        "PBE" // Public Beta Environment
    )
    var selectedServer by remember { mutableStateOf(servers[3]) } // 기본 KR

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.summoner_search_banner),
                contentDescription = "Summoner Search Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "search a summoner",
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedServer,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Server") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .width(100.dp)
                            .padding(end = 8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        servers.forEach { server ->
                            DropdownMenuItem(
                                text = { Text(server) },
                                onClick = {
                                    selectedServer = server
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Summoner Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummonerSearchScreenPreview() {
    SummonerSearchScreen()
} 