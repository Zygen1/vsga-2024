package com.lamdapratama.vsga2024;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addListFAB;
    private ListView lvList;
    private TextView totalTV;
    private MainAdapter adapter;
    private DBHelper dbHelper;

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

        addListFAB = findViewById(R.id.fabAddList);
        addListFAB.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TambahActivity.class);
            startActivity(i);
        });


        totalTV = findViewById(R.id.tvTotal);
        dbHelper = new DBHelper(this);

        lvList = findViewById(R.id.lvList);
        adapter = new MainAdapter(this);
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            Belanja belanja = adapter.getItem(position);

            // Inflate the bottom sheet layout
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_detail, null);

            // Initialize the bottom sheet dialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
            bottomSheetDialog.setContentView(bottomSheetView);

            // Set the item details in the bottom sheet
            TextView tvNama = bottomSheetView.findViewById(R.id.tvNama);
            TextView tvHarga = bottomSheetView.findViewById(R.id.tvHarga);
            TextView tvCatatan = bottomSheetView.findViewById(R.id.tvCatatan);
            Button btnEdit = bottomSheetView.findViewById(R.id.btnEdit);
            Button btnHapus = bottomSheetView.findViewById(R.id.btnHapus);

            tvNama.setText(belanja.getNama());

            // Format harga dengan titik setiap tiga digit
            tvHarga.setText("Rp." + formattedNumber(belanja.getHarga()));

            tvCatatan.setText(belanja.getCatatan());

            // Set the Edit button action
            btnEdit.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("id", belanja.getId());
                i.putExtra("nama", belanja.getNama());
                i.putExtra("harga", belanja.getHarga());
                i.putExtra("catatan", belanja.getCatatan());
                startActivity(i);
            });

            // Set the Delete button action
            btnHapus.setOnClickListener(v -> {
                dbHelper.hapusDaftar(belanja.getId());
                onResume();
                bottomSheetDialog.dismiss();
            });

            // Show the bottom sheet dialog
            bottomSheetDialog.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        adapter.addAll(dbHelper.getData());

        int total = 0;
        for (Belanja belanja : dbHelper.getData()) {
            total += belanja.getHarga();
        }

        totalTV.setText("Total: Rp." + formattedNumber(total));

        sortDataByPriceAscending();
    }

    private String formattedNumber(int harga) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedNumber = numberFormat.format(harga);
        return formattedNumber;
    }

    private void sortDataByPriceAscending() {
        ArrayList<Belanja> data = dbHelper.getDataSorted("price_asc");
        adapter.clear();
        adapter.addAll(data);
    }

    private void sortDataByPriceDescending() {
        ArrayList<Belanja> data = dbHelper.getDataSorted("price_desc");
        adapter.clear();
        adapter.addAll(data);
    }

    private void sortDataByName() {
        ArrayList<Belanja> data = dbHelper.getDataSorted("name");
        adapter.clear();
        adapter.addAll(data);
    }


}