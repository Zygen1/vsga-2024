package com.lamdapratama.vsga2024;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText etNama;
    private EditText etHarga;
    private EditText etCatatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);
        etNama = findViewById(R.id.etNamaActivityEdit);
        etHarga = findViewById(R.id.etHargaActivityEdit);
        etCatatan = findViewById(R.id.etCatatanActivityEdit);

        setupHargaTextWatcher();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            etNama.setText(extras.getString("nama"));
            etHarga.setText(String.valueOf(extras.getInt("harga")));
            etCatatan.setText(extras.getString("catatan"));
        }

    }

    public void cancelBTN(View view){
        finish();
    }

    public void updateBTN(View view){
        String id = getIntent().getExtras().getString("id");

        //sanity check
        String nama = etNama.getText().toString();
        if(nama.isEmpty()){
            showToast("Nama tidak boleh kosong");
            return;
        }

        String harga = etHarga.getText().toString();
        if (harga.isEmpty()) {
            showToast("Harga tidak boleh kosong");
            return;
        }
        String cleanHarga = harga.replaceAll("[Rp,.\\s]", "");
        int hargaInt =  Integer.parseInt(cleanHarga);

        String catatan = etCatatan.getText().toString();
        if (catatan.isEmpty()) {
            catatan = "-Tidak ada catatan-";
        }

        dbHelper.updateDaftar(id, nama, hargaInt, catatan);
        finish();
    }

    private void setupHargaTextWatcher(){
        etHarga.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    etHarga.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.\\s]", "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    // Gunakan Locale.GERMANY untuk mengubah koma menjadi titik
                    String formatted = String.format(Locale.GERMANY, "Rp %,.0f", parsed);

                    current = formatted;
                    etHarga.setText(formatted);
                    etHarga.setSelection(formatted.length());

                    etHarga.addTextChangedListener(this);
                }
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}