package com.example.myfirstkmpapp

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis


// ======================================================
// ======================= TUGAS 1 =======================
// ======================================================

data class Mahasiswa(val nama: String, val email: String?)

fun tugas1() {

    val daftarMhs = listOf(
        Mahasiswa("Budi", "budi@gmail.com"),
        Mahasiswa("Siti", null),
        Mahasiswa("Andi", "andi@yahoo.com")
    )

    println("\n--- TUGAS 1 : NULL SAFETY ---")

    for (mhs in daftarMhs) {
        val panjangEmail = mhs.email?.length ?: 0
        println("${mhs.nama} : Panjang email = $panjangEmail")
    }
}


// ======================================================
// ======================= TUGAS 2 =======================
// ======================================================

suspend fun getProfil(): String {
    delay(1000)
    return "Profil: Budi Santoso"
}

suspend fun getTransaksi(): String {
    delay(1000)
    return "Transaksi: [Beli Laptop, Beli Mouse]"
}

suspend fun tugas2() {

    println("\n--- TUGAS 2 : COROUTINE PARALEL ---")

    val waktu = measureTimeMillis {

        val profilDeferred = coroutineScope {
            async { getProfil() }
        }

        val transaksiDeferred = coroutineScope {
            async { getTransaksi() }
        }

        val profil = profilDeferred.await()
        val transaksi = transaksiDeferred.await()

        println("Hasil: $profil dan $transaksi")
    }

    println("Total Waktu: ${waktu}ms")
}


// ======================================================
// ======================= TUGAS 3 =======================
// ======================================================

fun sensorSuhu(): Flow<Int> = flow {

    val dataSuhu = listOf(28, 30, 36, 29, 40, 32, 45)

    for (suhu in dataSuhu) {
        delay(500)
        emit(suhu)
    }
}

suspend fun tugas3() {

    println("\n--- TUGAS 3 : FLOW ---")

    sensorSuhu()
        .filter { suhu -> suhu > 35 }
        .map { suhu ->
            "PERINGATAN! Suhu: $suhu derajat"
        }
        .collect { pesan ->
            println(pesan)
        }
}


// ======================================================
// ======================= MAIN ==========================
// ======================================================

fun main() = runBlocking {

    tugas1()
    tugas2()
    tugas3()

}
