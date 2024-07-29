package com.lamdapratama.vsga2024;

public class Catatan {

    private String nama;
    private String timestamp;

    public Catatan(String nama, String timestamp) {
        this.nama = nama;
        this.timestamp = timestamp;
    }

    public String getNama() {
        return nama;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
