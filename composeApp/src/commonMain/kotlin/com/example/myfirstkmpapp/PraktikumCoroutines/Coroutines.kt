package com.example.myfirstkmpapp.PraktikumCoroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
// Simulasi API call (JANGAN DIUBAH)
suspend fun getProfil(): String {
    delay(1000) // Pura-pura loading 1 detik
    return "Profil: Budi Santoso"
}
suspend fun getTransaksi(): String {
    delay(1000) // Pura-pura loading 1 detik
    return "Transaksi: [Beli Laptop, Beli Mouse]"
}
fun main() = runBlocking {
    println("--- MULAI DOWNLOAD DATA ---")
    val waktu = measureTimeMillis {
// TUGAS 2: Ubah kode di bawah ini agar berjalan PARALEL (Bersamaan)
// Gunakan `async` dan `await`
// --- UBAH BAGIAN INI ---
        // val profil = getProfil()
        // val transaksi = getTransaksi()

        val profilDeferred = async {getProfil()}
        val transaksiDeferred = async {getTransaksi()}

        val profil = profilDeferred.await()
        val transaksi = transaksiDeferred.await()
// -----------------------
        println("Hasil: $profil dan $transaksi")
    }
    println("Total Waktu: ${waktu}ms")
}
