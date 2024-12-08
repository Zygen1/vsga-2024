package com.lamdapratama.vsga2024;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addListFAB;
    private FloatingActionButton deleteAllFAB;
    private ListView lvList;
    private TextView totalTV;
    private MainAdapter adapterMain;
    private DBHelper dbHelper;
    private Spinner spSort;
    private EditText etUang, etKembalian;
    private int currentSort = 2; // Default filter (misalnya: urutan default)


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

        dbHelper = new DBHelper(this);
        initViews();
        setupSortSpinner();
        setupFAB();
        setupListView();
        setupUangListener();
    }

    private void initViews(){
        addListFAB = findViewById(R.id.fabAddList);
        deleteAllFAB = findViewById(R.id.fabDeleteAll);
        lvList = findViewById(R.id.lvList);
        totalTV = findViewById(R.id.tvTotal);
        spSort = findViewById(R.id.spSort);
        etUang = findViewById(R.id.etUang);
        etKembalian = findViewById(R.id.etKembalian);
    }

    private void setupUangListener() {
        etUang.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    etUang.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[^\\d]", "");
                    if (!cleanString.isEmpty()) {
                        int parsed = Integer.parseInt(cleanString);
                        String formatted = "Rp." + formattedNumber(parsed);
                        current = formatted;
                        etUang.setText(formatted);
                        etUang.setSelection(formatted.length());
                        calculateChange(parsed);
                    }

                    etUang.addTextChangedListener(this);
                }
            }
        });
    }

    private void calculateChange(int uang) {
        String totalText = totalTV.getText().toString().replaceAll("[^\\d]", "");
        int total = totalText.isEmpty() ? 0 : Integer.parseInt(totalText);

        if (uang <= 0) {
            etKembalian.setText("Kembalian");
        } else if (uang < total) {
            etKembalian.setText("Uang tidak cukup");
        } else {
            int kembalian = uang - total;
            etKembalian.setText("Rp." + formattedNumber(kembalian));
        }
    }



    private void setupSortSpinner(){
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.sort_array,
                android.R.layout.simple_spinner_item
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSort.setAdapter(adapterSpinner);
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSort = position; // Simpan posisi sortir
                applySort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentSort = 2; // Default sortir
                applySort(currentSort);
            }
        });
    }

    private void applySort(int position) {
        switch (position) {
            case 0: // Sort by price ascending
                sortDataByPriceAscending();
                break;
            case 1: // Sort by price descending
                sortDataByPriceDescending();
                break;
            default: // Default sorting
                sortDataDefault();
                break;
        }
    }

    private void setupFAB() {
        addListFAB.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TambahActivity.class);
            startActivity(i);
        });

        deleteAllFAB.setOnClickListener(v -> {
            String[] opsi = {"Ya", "Tidak"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Hapus Semua Daftar Belanja?");
            builder.setItems(opsi, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if ("Ya".equals(opsi[which])) {
                        dbHelper.hapusSemuaDaftar();
                        onResume();
                    } else if ("Tidak".equals(opsi[which])) {
                        //Do nothing
                    }
                }
            });
            builder.show();
        });
    }


    private void setupListView(){
        adapterMain = new MainAdapter(this);
        lvList.setAdapter(adapterMain);
        lvList.setOnItemClickListener((parent, view, position, id) -> {
            Belanja belanja = adapterMain.getItem(position);

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
        adapterMain.clear();
        adapterMain.addAll(dbHelper.getData());

        int total = 0;
        for (Belanja belanja : dbHelper.getData()) {
            total += belanja.getHarga();
        }

        totalTV.setText("Total: Rp." + formattedNumber(total));

        // Terapkan filter sesuai pilihan terakhir
        applySort(currentSort);

        // Recalculate change when the activity resumes
        String uangText = etUang.getText().toString().replaceAll("[^\\d]", "");
        int uang = uangText.isEmpty() ? 0 : Integer.parseInt(uangText);
        calculateChange(uang);
    }

    private String formattedNumber(int harga) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedNumber = numberFormat.format(harga);
        return formattedNumber;
    }

    private void sortDataByPriceAscending() {
        ArrayList<Belanja> data = dbHelper.getDataSorted("price_asc");
        adapterMain.clear();
        adapterMain.addAll(data);
    }

    private void sortDataByPriceDescending() {
        ArrayList<Belanja> data = dbHelper.getDataSorted("price_desc");
        adapterMain.clear();
        adapterMain.addAll(data);
    }

    private void sortDataDefault(){
        ArrayList<Belanja> data = dbHelper.getDataSorted("default");
        adapterMain.clear();
        adapterMain.addAll(data);
    }
}