package com.lamdapratama.vsga2024;

public class Belanja {
    private String id;
    private String nama;
    private int harga;
    private String catatan;

    public Belanja(String id, String nama, int harga, String catatan) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.catatan = catatan;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public String getCatatan() {
        return catatan;
    }
}
