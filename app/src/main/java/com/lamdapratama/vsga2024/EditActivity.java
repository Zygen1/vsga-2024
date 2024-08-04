package com.lamdapratama.vsga2024;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    private DBHelper dbhelper;
    private EditText namaEditText;
    private EditText alamatEditText;

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

        dbhelper = new DBHelper(this);
        alamatEditText = findViewById(R.id.updateAlamatEditText);
        namaEditText = findViewById(R.id.updateNamaEditText);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            alamatEditText.setText(extras.getString("alamat"));
            namaEditText.setText(extras.getString("nama"));
        }
    }

    public void cancel(View view){
        finish();
    }

    public void update(View view){
        String id = getIntent().getExtras().getString("id");

        //sanity check
        String nama = namaEditText.getText().toString();
        if(nama.isEmpty()){
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        String alamat = alamatEditText.getText().toString();
        if(alamat.isEmpty()){
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        dbhelper.updateKontak(id, nama, alamat);
        finish();
    }
}