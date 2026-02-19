package com.example.myfirstkmpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val PeachPinkColorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFFFF8A80),
            onPrimary = androidx.compose.ui.graphics.Color.White,
            secondary = androidx.compose.ui.graphics.Color(0xFFFFAB91),
            onSecondary = androidx.compose.ui.graphics.Color.White,
            background = androidx.compose.ui.graphics.Color(0xFFFFF0F5),
            surface = androidx.compose.ui.graphics.Color(0xFFFFE4E1),
            surfaceVariant = androidx.compose.ui.graphics.Color(0xFFFFD1DC),
            onSurface = androidx.compose.ui.graphics.Color(0xFF4A2C2A),
            onBackground = androidx.compose.ui.graphics.Color(0xFF4A2C2A)
        )

        setContent {

            val beritaList = remember { mutableStateListOf<Berita>() }
            var jumlahDibaca by remember { mutableStateOf(0) }
            var kategoriFilter by remember { mutableStateOf("Semua") }
            val expandedMap = remember { mutableStateMapOf<String, Boolean>() }
            val detailMap = remember { mutableStateMapOf<String, String>() }
            val scope = rememberCoroutineScope()

            MaterialTheme(
                colorScheme = PeachPinkColorScheme
            ) {

                LaunchedEffect(Unit) {
                    arusBerita()
                        .filter { kategoriFilter == "Semua" || it.kategori == kategoriFilter }
                        .collect { berita ->
                            beritaList.add(berita)
                        }
                }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("News Feed ($jumlahDibaca)") },
                            actions = {
                                Text(
                                    text = "LIVE",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                            }
                        )
                    }
                ) { padding ->

                    LazyColumn(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                    ) {

                        items(beritaList) { berita ->

                            val expanded = expandedMap[berita.judul] ?: false
                            val detailText = detailMap[berita.judul] ?: ""

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                onClick = {
                                    if (!expanded) {
                                        jumlahDibaca++
                                        expandedMap[berita.judul] = true

                                        scope.launch {
                                            val detail = ambilDetail(berita)
                                            detailMap[berita.judul] = detail
                                        }
                                    }
                                }
                            ) {

                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {

                                    // Kategori kecil seperti badge berita
                                    Text(
                                        text = berita.kategori.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Judul besar
                                    Text(
                                        text = berita.judul,
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (expanded) {
                                        Text(
                                            text = detailText,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    } else {
                                        Text(
                                            text = "Ketuk untuk membaca selengkapnya...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )
                        }
                    }
                }
            }
        }
    }
}
