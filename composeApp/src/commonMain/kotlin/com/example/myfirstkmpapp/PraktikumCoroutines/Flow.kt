package com.example.myfirstkmpapp.PraktikumCoroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
// Simulasi Sensor (Producer)
fun sensorSuhu(): Flow<Int> = flow {
    val dataSuhu = listOf(28, 30, 36, 29, 40, 32, 45)
    for (suhu in dataSuhu) {
        delay(500) // Sensor update tiap 0.5 detik
        emit(suhu) // Kirim data
    }
}
fun main() = runBlocking {
    println("--- MONITOR SUHU AKTIF ---")
    sensorSuhu()
// TUGAS 3a: Filter data. Hanya loloskan suhu > 35
// .filter { ... }
        .filter { suhu -> suhu > 35 }
// TUGAS 3b: Ubah format data jadi String "PERINGATAN! Suhu: X derajat"
// .map { ... }
        .map { suhu ->
            "PERINGATAN! Suhu: $suhu derajat"
        }

        .collect { pesan ->
            println(pesan)
        }
}
