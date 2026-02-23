package com.example.inventory_control.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventory_control.R;

public class StockPrepProductActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnClear, btnSave;
    private Switch switchRfid;
    private EditText resultScan;
    private TextView tvScanned, tvNoDo, tvDateDo;

    private int scanCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastiin XML ini nampilin detail produk (yang ada tombol Clear/Save)
        setContentView(R.layout.activity_stock_prep_product);

        // 1. Inisialisasi View
        btnBack = findViewById(R.id.btnBack);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);
        switchRfid = findViewById(R.id.switchRfid);
        resultScan = findViewById(R.id.resultScan);
        tvScanned = findViewById(R.id.tvScanned);
        tvNoDo = findViewById(R.id.tvNoDo);
        tvDateDo = findViewById(R.id.tvDateDo);

        // 2. Tangkep data DO dari halaman sebelumnya (kalau ada)
        Intent intent = getIntent();
        if (intent != null) {
            String passedNoDo = intent.getStringExtra("NO_DO");
            String passedDateDo = intent.getStringExtra("DATE_DO");

            if (passedNoDo != null) tvNoDo.setText("No : " + passedNoDo);
            if (passedDateDo != null) tvDateDo.setText("Date : " + passedDateDo);
        }

        // 3. Logic Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 4. Logic Tombol Clear & Save
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCount = 0;
                tvScanned.setText("Scanned : " + scanCount);
                Toast.makeText(StockPrepProductActivity.this, "Data dibersihkan", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StockPrepProductActivity.this, "Stock Preparation Disimpan", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Logic Switch RFID
        switchRfid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(StockPrepProductActivity.this, "RFID: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
            }
        });

        // 6. Logic Scanner Hardware (Sama kayak Stock In)
        resultScan.requestFocus();
        resultScan.setShowSoftInputOnFocus(false);
        resultScan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    String rfidData = resultScan.getText().toString().trim();
                    if (!rfidData.isEmpty()) {
                        scanCount++;
                        tvScanned.setText("Scanned : " + scanCount);
                        Toast.makeText(StockPrepProductActivity.this, "Scan: " + rfidData, Toast.LENGTH_SHORT).show();
                        resultScan.setText("");
                    }
                    resultScan.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }
}