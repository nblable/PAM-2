package com.example.myfirstkmpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val beritaList = remember { mutableStateListOf<Berita>() }
            var jumlahDibaca by remember { mutableStateOf(0) }
            var kategoriFilter by remember { mutableStateOf("Semua") }
            val expandedMap = remember { mutableStateMapOf<String, Boolean>() }
            val detailMap = remember { mutableStateMapOf<String, String>() }
            val scope = rememberCoroutineScope()


            LaunchedEffect(Unit) {
                arusBerita()
                    .filter { kategoriFilter == "Semua"|| it.kategori == kategoriFilter }
                    .collect { berita ->
                        beritaList.add(berita)
                    }
            }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("News Feed ($jumlahDibaca)") }
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
                                .padding(bottom = 14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                AssistChip(
                                    onClick = {},
                                    label = { Text(berita.kategori) }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = berita.judul,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
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
                                    Text(
                                        if (expanded) "Sudah Dibaca"
                                        else "Baca Selengkapnya"
                                    )
                                }

                                if (expanded) {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = detailText,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
