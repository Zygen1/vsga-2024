package com.lamdapratama.vsga2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        adapter = new MainAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.addAll(getCatatan());
    }

    private ArrayList<Catatan> getCatatan(){
        ArrayList<Catatan> catatan = new ArrayList<>();

        File directory = new File(getFilesDir() + "/catatan");
        File[] files = directory.listFiles();
        if(files == null){
            return catatan;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        for (File file : files) {
            String namaFile = file.getName();
            Date date = new Date(file.lastModified());
            String timestamp = formatter.format(date);
            catatan.add(new Catatan(namaFile, timestamp));
        }

        return catatan;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_tambah){
            Intent i = new Intent(this, TambahActivity.class);
            startActivity(i);
            return true;
        }
        return false;
    }
}