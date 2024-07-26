package com.lamdapratama.vsga2024;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {


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

        editText = findViewById(R.id.edit_text);

        bacaFile();
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

    private void bacaFile(){
        File file = new File(getFilesDir(), FILENAME);
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
            }catch(IOException e){
                e.printStackTrace();
            }
            editText.setText(text);
        }else{
            editText.setText("");
        }
    }

    private void hapusFile(){
        File file = new File(getFilesDir(), FILENAME);
        if(file.exists()){
            file.delete();
            editText.setText("");
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.btn_buat_file){
            buatFile();
        }else if(view.getId() == R.id.btn_baca_file){
            bacaFile();
        }
        else if(view.getId() == R.id.btn_hapus_file){
            hapusFile();
        }
    }
}