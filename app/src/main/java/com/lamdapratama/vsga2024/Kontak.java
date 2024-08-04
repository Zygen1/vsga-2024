package com.lamdapratama.vsga2024;

public class Kontak {
    private String id;
    private String nama;
    private String alamat;

    public Kontak(String id, String nama, String alamat) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getId(){
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }
}
