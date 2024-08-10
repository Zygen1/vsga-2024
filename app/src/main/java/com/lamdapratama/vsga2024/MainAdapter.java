package com.lamdapratama.vsga2024;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MainAdapter extends ArrayAdapter<Belanja> {

    public MainAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<>());
    }

}
