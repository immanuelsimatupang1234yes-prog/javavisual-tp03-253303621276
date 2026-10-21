# Character Manager — TP3 (Java Visual SIH-6406)

Proyek ini adalah fondasi model class untuk aplikasi Character Manager
berbasis Java 17. Berisi enum `CharacterType` dan class `Character` dengan
dua constructor, validasi pada setter, serta fitur tambahan `mana` dan
`isAlive()`. Class ini akan menjadi dasar GUI pada sprint berikutnya.

## Output Terminal

![Output terminal Character Manager](screenshots/output.png)

## Struktur Folder

```
javavisual-tp03-253303621276/
├── README.md
├── .gitignore
├── screenshots/
│   └── output.png
└── CharacterManager/
    └── src/
        ├── Main.java
        └── com/unpri/charactermanager/model/
            ├── CharacterType.java
            └── Character.java
```

## Identitas

- Nama: Imanuel Christ P Simatupang
- NIM: 253303621276
- Analisis Bagian D (Google Docs): https://docs.google.com/document/d/1TI8xPdEMy4z-I7K7wC-oL94S7rXh3nMTShnTTqvBsds/edit?usp=sharing

# Character Manager — TP3 (Java Visual SIH-6406)

Proyek ini adalah fondasi model class untuk aplikasi Character Manager
berbasis Java 17. Berisi enum `CharacterType` dan class `Character` dengan
dua constructor, validasi pada setter, serta fitur tambahan `mana` dan
`isAlive()`. Class ini akan menjadi dasar GUI pada sprint berikutnya.

## Output Terminal (TP3)

![Output terminal Character Manager](screenshots/output.png)

## Identitas

- Nama: Imanuel Christ P Simatupang
- NIM: 253303621276
- Analisis Bagian D TP3 (Google Docs): [link Google Docs TP3 yang sudah ada]

---

## TP5 — Character Manager Sprint #2

Pada sprint ini ditambahkan class `CharacterManager` yang mengelola koleksi
`Character` memakai `ArrayList`, dengan penolakan nama duplikat lewat `equals()`,
`getAllCharacters()` yang mengembalikan salinan (defensive copy), serta counter
`static` untuk jumlah objek yang dibuat.

### Fitur baru

- `equals()` pada `Character` membandingkan nama, sehingga nama duplikat ditolak
- `totalCharacterCreated` (`static`) menghitung jumlah objek `Character` yang dibuat
- `CharacterManager`: `addCharacter()`, `findByName()`, `removeCharacter()`,
  `getTotalCharacters()`, `getAllCharacters()`
- Mini-Challenge: `getAverageLevel()` (mengembalikan 0 jika list kosong) dan `findByType()`
- Fitur mandiri: `getTypeSummary()`

### Screenshot output (CRUD dan eksperimen reference)

![Output TP5: CRUD, penolakan duplikat, dan eksperimen reference](screenshots/output-tp5.png)

Screenshot memperlihatkan nama duplikat ditolak, `getAverageLevel()` pada list kosong
menghasilkan `0.0` tanpa error, serta eksperimen reference: `asli == duplikatReferensi`
bernilai `true`, sedangkan menambah karakter ke salinan list tidak mengubah total
di manager (manager tetap 2, salinan menjadi 3).

### Fitur mandiri (Bagian C): `getTypeSummary()`

`getTypeSummary()` merangkum jumlah karakter untuk setiap tipe (WARRIOR, MAGE,
ARCHER, HEALER) dalam satu `String`, misalnya untuk ditampilkan di GUI pada sprint
berikutnya. Method ini mengembalikan `String`, bukan `characterList` atau salinannya,
karena `String` tidak dapat diubah dari luar. Pemanggil hanya menerima hasil hitungan
dan tidak pernah memegang reference ke list internal, sehingga Encapsulation terjaga
dan perubahan data hanya bisa lewat `addCharacter()` dan `removeCharacter()`. Method
memakai `CharacterType.values()`, sehingga jika kelak ditambah tipe baru seperti
NECROMANCER, method ini otomatis ikut menghitungnya tanpa perlu diubah.

### Struktur folder

```
javavisual-tp03/
├── README.md
├── .gitignore
├── screenshots/
│   ├── output.png
│   └── output-tp5.png
└── CharacterManager/
    └── src/
        ├── Main.java
        └── com/unpri/charactermanager/model/
            ├── CharacterType.java
            ├── Character.java
            └── CharacterManager.java
```

### Analisis Bagian D TP5 (Google Docs)

https://docs.google.com/document/d/1lNYlKdLlfM6yDAO85SyTOmQBv9GevR5dmRCPF70nRhk/edit?usp=sharing


---

## TP8 — Character Manager Sprint #3

Pada sprint ini aplikasi berpindah dari console ke GUI. Ditambahkan class
`MainWindow` (package `view`, `extends JFrame`) yang menampilkan daftar karakter
secara dinamis lewat loop, ditambah statistik "Rata-rata Level" di bawah daftar.
`Main.java` menjalankan jendela lewat `SwingUtilities.invokeLater()`.

### Screenshot MainWindow

Sebelum data diubah (Gandalf level 7, rata-rata level 4.20):

![MainWindow sebelum data diubah](screenshots/tp8-sebelum.png)

Sesudah data diubah (Gandalf level 10, rata-rata level 4.80):

![MainWindow sesudah data diubah](screenshots/tp8-sesudah.png)

Kedua screenshot memuat 5 karakter dan membuktikan bahwa statistik ikut berubah
saat data diubah.

### Aliran data: Main → CharacterManager → MainWindow

`Main.java` membuat `CharacterManager` dan mengisinya dengan lima karakter (Model).
Data diambil lewat `manager.getAllCharacters()`, yang mengembalikan salinan list,
lalu diteruskan ke constructor `MainWindow`. `MainWindow` (View) hanya menerima
list itu, membuat satu `JLabel` untuk setiap karakter lewat loop, dan menampilkan
rata-rata level, tanpa mengetahui cara data disimpan. Dalam MVC, `Main` untuk
sementara berperan sebagai penghubung (pengganti Controller yang dibangun pada
pertemuan berikutnya): Model tidak mengenal View, dan View tidak mengubah data Model.

### Karakter baru

| Karakter | Tipe | Level | Alasan pemilihan |
|---|---|---|---|
| Elara | HEALER | 2 | Melengkapi empat tipe karakter (sebelumnya belum ada HEALER) dan level rendah agar datanya bervariasi |
| Gandalf | MAGE | 7, lalu diubah menjadi 10 | Level tinggi sehingga pengaruhnya pada rata-rata level terlihat jelas saat data diubah |

### Struktur folder

```
javavisual-tp03/
├── README.md
├── .gitignore
├── screenshots/
│   ├── output.png
│   ├── output-tp5.png
│   ├── tp8-sebelum.png
│   └── tp8-sesudah.png
└── CharacterManager/
    └── src/
        ├── Main.java
        └── com/unpri/charactermanager/
            ├── model/
            │   ├── CharacterType.java
            │   ├── Character.java
            │   └── CharacterManager.java
            └── view/
                └── MainWindow.java
```

### Analisis Bagian D TP8 (Google Docs)

[https://docs.google.com/document/d/1KiLVY2qjljAQn7smLqUMAAdyR7t12abo-elTMkI_t14/edit?usp=sharing]