package com.example.myfirstkmpapp

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class Berita(
    val judul: String,
    val kategori: String,
    val detail: String
)

fun arusBerita(): Flow<Berita> = flow {
    val data = listOf(
        Berita("King MU Menang", "Olahraga", "MU menang 4 kali beruntun semenjak di latih oleh carrick"),
        Berita("Awal Puasa Berbeda", "Agama", "Awal puasa berbeda karena Muhammadiyah di laksanakan di tanggal 18 Februari 2026 sedangkan untuk NU di tanggal 19 Februari 2026"),
        Berita("IHSG Kembali Menguat","Ekonomi", "IHSG kembali menguat berkat kerja keras dari menteri keungan kita yaitu Purbaya Sadewa"),
        Berita("GPT vs Gemini","Teknologi", "GPT dan Gemini seringkali bersaing dengan keras"),
        Berita("Decul Nangis","Olahraga", "Barcelona kembali kalah dalam laga Barcelona vs ATM dengan skor 4-0 unggul ATM")
    )

    for (berita in data) {
        delay(2000)
        emit(berita)
    }
} .flowOn(Dispatchers.Default)

suspend fun ambilDetail(berita: Berita): String =
    withContext(Dispatchers.IO) {
        delay(1000)
        "Detail berita: ${berita.detail}"
    }

fun main() = runBlocking {
    println("---NEWS FEED---")
    val jumlahDibaca = MutableStateFlow(0)

    launch {
        jumlahDibaca.collect {
            println("Jumlah dibaca: $it")
        }
    }

    val kategoriFilter ="Semua"
    arusBerita()
        .filter { kategoriFilter == "Semua" || it.kategori == kategoriFilter }
        .map { "[${it.kategori}] ${it.judul}" }
        .onEach { println("Diproses -> $it") }
        .catch { e -> println("Error terjadi: ${e.message}") }

        .collect { tampil -> println("Tampil -> $tampil")
            jumlahDibaca.value++

            val detail = async {
                ambilDetail(
                        Berita(tampil, "Olahraga", "")
                )
            }

            println(detail.await())
        }
}
