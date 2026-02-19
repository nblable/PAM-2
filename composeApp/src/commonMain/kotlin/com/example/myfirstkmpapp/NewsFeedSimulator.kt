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
        Berita("NABILA CANTIK", "Kecantikan", "Cantik itu bukan hanya berasal dari luar tetapi dari dalam juga"),
        Berita("BAJU LEBARAN", "Fashion", "Jika ingin membeli baju lebaran beli di Ethica"),
        Berita("JAM TANGAN","Waktu", "Waktu adalah uang, karena saya seorang BENDUM")
    )

    for (berita in data) {
        delay(2000)
        emit(berita)
    }
} .flowOn(Dispatchers.Default)

suspend fun ambilDetail(berita: Berita): String =
    withContext(Dispatchers.IO) {
        delay(1000)
        "Berita lengkap: ${berita.detail}"
    }

fun main() = runBlocking {
    println("===Arus Berita===")
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
                        Berita(tampil, "Semua", "")
                )
            }

            println(detail.await())
        }
}
