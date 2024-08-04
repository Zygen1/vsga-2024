package com.lamdapratama.vsga2024;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ListView listView;
    private MainAdapter adapter;
    private DBHelper dbhelper;

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

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TambahActivity.class);
            startActivity(i);
        });

        dbhelper = new DBHelper(this);
        listView = findViewById(R.id.listView);
        adapter = new MainAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] opsi = {"Edit", "Hapus"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Dipilih: " + adapter.getItem(position).getNama());
                builder.setItems(opsi, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if("Edit".equals(opsi[which])){
                            Intent i = new Intent(
                                    MainActivity.this,
                                    EditActivity.class);
                            i.putExtra("id", adapter.getItem(position).getId());
                            i.putExtra("nama", adapter.getItem(position).getNama());
                            i.putExtra("alamat", adapter.getItem(position).getAlamat());
                            startActivity(i);
                        }
                        else if("Hapus".equals(opsi[which])){
                            dbhelper.hapusKontak(adapter.getItem(position).getId());
                            onResume();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.addAll(dbhelper.getData());
    }
}