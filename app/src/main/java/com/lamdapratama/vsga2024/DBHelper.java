package com.lamdapratama.vsga2024;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE =
            "CREATE TABLE daftar_belanja (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nama TEXT, " +
                    "harga INTEGER, " +
                    "catatan TEXT" +
                    ")";

    public DBHelper(@Nullable Context context) {
        super(context, "dataBelanja.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public long tambahDaftar(String nama, int harga, String catatan) {
        ContentValues values = new ContentValues();
        values.put("nama", nama);
        values.put("harga", harga);
        values.put("catatan", catatan);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert("daftar_belanja", null, values);
    }

    public long updateDaftar(String id, String nama, int harga, String catatan) {
        ContentValues values = new ContentValues();
        values.put("nama", nama);
        values.put("harga", harga);
        values.put("catatan", catatan);

        SQLiteDatabase db = getWritableDatabase();
        return db.update("daftar_belanja", values, "id=?", new String[]{id});
    }

    public long hapusDaftar(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("daftar_belanja", "id=?", new String[]{id});
    }

    public ArrayList<Belanja> getData(){
        ArrayList<Belanja> result = new ArrayList<>();

        String query = "SELECT * FROM daftar_belanja";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String catatan = cursor.getString(3);
            result.add(new Belanja(id, nama, harga, catatan));
        }

        cursor.close();

        return result;
    }

    public ArrayList<Belanja> getDataSorted(String sortBy) {
        ArrayList<Belanja> belanjaList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM daftar_belanja";

        switch (sortBy) {
            case "price_asc":
                query += " ORDER BY harga ASC";
                break;
            case "price_desc":
                query += " ORDER BY harga DESC";
                break;
            case "name":
                query += " ORDER BY nama ASC";
                break;
        }

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String nama = cursor.getString(1);
                int harga = cursor.getInt(2);
                String catatan = cursor.getString(3);
                belanjaList.add(new Belanja(id, nama, harga, catatan));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return belanjaList;
    }

}
