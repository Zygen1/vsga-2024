package com.lamdapratama.vsga2024;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    /*public static final String FILENAME = "nama_file.txt";
    Button buatFile, ubahFile, bacaFile, hapusFile;
    TextView textBaca;*/

    private static final String FILENAME = "catatan.text";
    private EditText editText;

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


        /*buatFile = findViewById(R.id.btn_buat_file);
        ubahFile = findViewById(R.id.btn_ubah_file);
        bacaFile = findViewById(R.id.btn_baca_file);
        hapusFile = findViewById(R.id.btn_hapus_file);
        textBaca = findViewById(R.id.text_baca);

        buatFile.setOnClickListener(this);
        ubahFile.setOnClickListener(this);
        bacaFile.setOnClickListener(this);
        hapusFile.setOnClickListener(this);*/

        editText = findViewById(R.id.edit_text);
    }

    private void buatFile(){
        File file = new File(getFilesDir(), FILENAME);
        FileOutputStream fos;

        try{
            file.createNewFile();
            fos = new FileOutputStream(file, false);
            fos.write(editText.getText().toString().getBytes());
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.btn_buat_file){
            buatFile();
        }
    }

    /*void buatFile() {
        String isiFile = "Coba isi Data File Text";
        File file = new File(getFilesDir(), FILENAME);

        FileOutputStream outputStream;
        try{
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void ubahFile() {
        String ubah = "Update Isi Data File Text";
        File file = new File(getFilesDir(), FILENAME);
        FileOutputStream outputStream;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(ubah.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void bacaFile(){
        File sdCard = getFilesDir();
        File file = new File(sdCard, FILENAME);

        if(file.exists()){
            StringBuilder text = new StringBuilder();

            try{
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line = br.readLine();

                while(line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            textBaca.setText(text.toString());
        }
    }

    void hapusFile(){
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onClick(View v) {
        jalankanPerintah(v.getId());
    }

    public void jalankanPerintah(int id) {
        if(id == R.id.btn_buat_file){
            buatFile();
        }else if(id == R.id.btn_baca_file){
            bacaFile();
        }else if(id == R.id.btn_ubah_file){
            ubahFile();
        }else if(id == R.id.btn_hapus_file){
            hapusFile();
        }
    }*/
}