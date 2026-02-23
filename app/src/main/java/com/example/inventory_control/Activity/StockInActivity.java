package com.example.inventory_control.Activity;

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
import com.google.android.material.snackbar.Snackbar;

public class StockInActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnClear, btnSave;
    private Switch switchRfid;
    private EditText resultScan;
    private TextView tvScanned;
    private int scanCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);

        // 1. Inisialisasi View
        btnBack = findViewById(R.id.btnBack);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);
        switchRfid = findViewById(R.id.switchRfid);
        resultScan = findViewById(R.id.resultScan);
        tvScanned = findViewById(R.id.tvScanned);

        resultScan.requestFocus();
        resultScan.setShowSoftInputOnFocus(false);

        resultScan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Scanner biasanya ngirim event KEYCODE_ENTER atau action done/next
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    // Ambil hasil scan-nya
                    String rfidData = resultScan.getText().toString().trim();

                    if (!rfidData.isEmpty()) {

                        // Update angka di TextView Scanned
                        scanCount++;
                        tvScanned.setText("Scanned: " + scanCount);

                        Toast.makeText(StockInActivity.this, "Berhasil scan: " + rfidData, Toast.LENGTH_SHORT).show();

                        // Kosongin lagi EditText-nya buat nangkep scan berikutnya
                        resultScan.setText("");
                    }

                    // Balikin fokus ke resultScan lagi biar siap scan barang ke-2
                    resultScan.requestFocus();
                    return true;
                }
                return false;
            }
        });        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish() berfungsi buat nutup halaman saat ini dan otomatis balik ke halaman sebelumnya
                finish();
            }
        });

        // 3. Logic Switch RFID (Deteksi ON / OFF)
        switchRfid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Angka 1000 di belakang itu durasinya = 1000 milidetik (1 detik)
                    Snackbar.make(findViewById(android.R.id.content), "Mode RFID: ON", 1000).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Mode RFID: OFF", 1000).show();
                }
            }
        });

        // 4. Logic Tombol Clear
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nanti code buat hapus isi list/RecyclerView ditaruh di sini
                Toast.makeText(StockInActivity.this, "List dibersihkan", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Logic Tombol Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nanti code buat nge-hit API Simpan Data ditaruh di sini
                Toast.makeText(StockInActivity.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}