# Apadok Android
Langkah - langkah untuk menjalankan aplikasi:

1. Lakukan instalasi Android Studio. Android studio dapat di-download pada: https://developer.android.com/studio
2. Lakukan clone untuk project Apadok
3. pada file local.properties, tambahkan line berikut:
- storeFile=keygen.jks
- keyAlias=key0
- keyPassword=keydokks
- storePassword=keydokks
4. Lakukan proses build dan jalankan aplikasi menggunakan android studio

Dokumentasi API: **https://google.github.io/volley/**
List API di grup line chat 2 Februari

TO-DO LIST:
1. Add Room Database (WIP) - Room Database dipakai untuk mode offline kalau masih diminta dokter
2. Change Chat WebSocket into Background Service - https://developer.android.com/guide/components/services
3. Add push notification for Chat WebSocket
4. Improve on UI & UX

* Key store path pas build buat playstore ada di folder app/keygen.jks
* Pas upload .aab ada difolder app/release/app-release.aab
