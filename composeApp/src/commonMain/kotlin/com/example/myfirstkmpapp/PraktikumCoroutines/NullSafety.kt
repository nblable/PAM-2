package com.example.myfirstkmpapp.PraktikumCoroutines

data class Mahasiswa(val nama: String, val email: String?)
fun main() {
    val daftarMhs = listOf(
        Mahasiswa("Budi", "budi@gmail.com"),
        Mahasiswa("Siti", null), // Email kosong
        Mahasiswa("Andi", "andi@yahoo.com")
    )
    println("--- DAFTAR EMAIL MAHASISWA ---")
    for (mhs in daftarMhs) {
// TUGAS 1: Ambil panjang karakter email.
// Jika email null, anggap panjangnya 0.
// HINT: Gunakan Safe Call (?.) dan Elvis Operator (?:)
        // val panjangEmail = 0 // <--- GANTI BARIS INI DENGAN KODE ANDA
        val panjangEmail = mhs.email?.length ?: 0

        println("${mhs.nama} : Panjang email = $panjangEmail")
    }
}
